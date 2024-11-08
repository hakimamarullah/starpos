package com.starline.views.products;

import com.starline.components.Container;
import com.starline.components.NotificationUtil;
import com.starline.data.Product;
import com.starline.services.ProductService;
import com.starline.utils.CommonUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PageTitle("Products")
@Route("products")
@Menu(order = 1, icon = "line-awesome/svg/product-hunt.svg")
@RolesAllowed("USER")
public class ProductsView extends Composite<VerticalLayout> {

    private static final Logger log = LoggerFactory.getLogger(ProductsView.class);
    private final Container container;

    private final transient ProductService productService;

    private final Set<Product> selectedProducts = new HashSet<>();
    private Grid<Product> productGrid;

    private TextField productSearchField;

    public ProductsView(ProductService productService) {
        this.productService = productService;
        container = new Container();
        container.setCaption("Add Product");
        container.getContainerStyle().setFlexDirection(Style.FlexDirection.COLUMN);

        Dialog formAddProduct = initFormAddProduct();

        Button newProductBtn = new Button("New", e -> formAddProduct.open());
        Button archiveProductBtn = new Button("Archive", e -> doArchiveProduct(selectedProducts));
        Button publishProductBtn = new Button("Publish", e -> doPublishProduct(selectedProducts));
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.getStyle().setJustifyContent(Style.JustifyContent.LEFT);
        btnLayout.getStyle().setAlignItems(Style.AlignItems.CENTER);
        btnLayout.setWidthFull();
        btnLayout.add(newProductBtn, archiveProductBtn, publishProductBtn);
        addContent(formAddProduct);
        addContent(btnLayout);


        HorizontalLayout gridContainer = new HorizontalLayout();
        gridContainer.setWidthFull();
        gridContainer.getStyle().setJustifyContent(Style.JustifyContent.LEFT);
        gridContainer.getStyle().setFlexDirection(Style.FlexDirection.COLUMN);
        gridContainer.getStyle().setAlignItems(Style.AlignItems.CENTER);
        initProductGrid();

        HorizontalLayout searchFieldContainer = new HorizontalLayout();
        searchFieldContainer.setWidthFull();
        searchFieldContainer.getStyle().setJustifyContent(Style.JustifyContent.LEFT);
        searchFieldContainer.getStyle().setAlignItems(Style.AlignItems.CENTER);
        searchFieldContainer.add(productSearchField);

        gridContainer.add(searchFieldContainer);
        gridContainer.add(productGrid);

        addContent(gridContainer);

        getContent().add(container);
    }

    void addContent(Component... components) {
        this.container.addContent(components);
    }


    Dialog initFormAddProduct() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add Product");
        dialog.setCloseOnEsc(true);
        dialog.setWidth("400px");

        FormLayout formLayout = new FormLayout();


        TextField code = new TextField();
        code.setRequired(true);
        code.setErrorMessage("Product code is mandatory");
        code.setRequiredIndicatorVisible(true);

        TextField name = new TextField();
        name.setRequired(true);
        name.setErrorMessage("Name can't be blank");
        name.setMinLength(2);
        name.setRequiredIndicatorVisible(true);

        NumberField price = new NumberField();
        price.setRequiredIndicatorVisible(true);
        price.setMin(0.0);
        price.setErrorMessage("Price can't be smaller than 0");
        price.setRequired(true);

        TextField description = new TextField();
        description.setMaxLength(255);


        formLayout.addFormItem(code, "Code");
        formLayout.addFormItem(name, "Name");
        formLayout.addFormItem(price, "Price");
        formLayout.addFormItem(description, "Description");


        dialog.add(formLayout);

        Runnable reset = () -> {
            code.clear();
            name.clear();
            price.clear();
            description.clear();
        };

        Button submit = new Button("Save");
        submit.addClickListener(e -> {
            Product product = new Product();
            product.setCode(code.getValue());
            product.setName(name.getValue());
            product.setPrice(price.getValue());
            product.setDescription(description.getValue());


            if (Boolean.FALSE.equals(checkIsValid(code, name, price, description))) {
                return;
            }

            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            if (!validatorFactory.getValidator().validate(product).isEmpty()) {
                NotificationUtil.showError("Please fill all data correctly!");
                return;
            }
            if (Boolean.TRUE.equals(doSaveProduct(product))) {
                dialog.close();
                reset.run();
                productGrid.getDataProvider().refreshAll();
            }
        });

        Button cancel = new Button("Cancel", e -> {
            dialog.close();
            reset.run();
        });

        dialog.getFooter().add(cancel, submit);


        return dialog;
    }

    void initProductGrid() {
        productGrid = new Grid<>(Product.class, false);
        BiFunction<Query<Product,Void>, String, Stream<Product>> searchDataProvider = (query, searchKey) -> {
            Sort sort = CommonUtil.toJpaSort(query.getSortOrders())
                    .orElse(Sort.by(new Sort.Order(Sort.Direction.ASC, "isDeleted"), new Sort.Order(Sort.Direction.DESC, "createdTime")));
            Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize(), sort);

            if (StringUtils.isBlank(searchKey)) {
                return productService.list(pageable).getContent().stream();
            }
            return productService.findByCodeOrNameContains(searchKey, pageable).getContent().stream();
        };

        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        productGrid.setWidth("65%");
        productGrid.setItems(query -> searchDataProvider.apply(query, null));

        SerializableBiConsumer<Span, Product> deleteFlagUpdater = (span, product) -> {
            String theme = String.format("badge %s", Boolean.TRUE.equals(product.isDeleted()) ? "error" : "success");
            span.getElement().setAttribute("theme", theme);
            span.setText(Boolean.TRUE.equals(product.isDeleted()) ? "Out-of-Stock" : "In-Stock");
        };

        var codeCol = productGrid.addColumn(Product::getCode)
                .setHeader("Code")
                .setSortable(true)
                .setTextAlign(ColumnTextAlign.START)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setSortProperty("code");
        var nameCol = productGrid.addColumn(Product::getName)
                .setHeader("Name")
                .setTextAlign(ColumnTextAlign.START)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setSortable(Boolean.TRUE)
                .setSortProperty("name");
        var priceCol = productGrid.addColumn(new NumberRenderer<>(Product::getPrice, Locale.of("id", "ID")))
                .setHeader("Price")
                .setTextAlign(ColumnTextAlign.START)
                .setSortable(Boolean.TRUE)
                .setSortProperty("price")
                .setAutoWidth(true)
                .setFlexGrow(0);
        var descriptionCol = productGrid.addColumn(Product::getDescription).setHeader("Description").setAutoWidth(true).setFlexGrow(0);
        productGrid.addColumn(new ComponentRenderer<>(Span::new, deleteFlagUpdater))
                .setHeader("Status")
                .setSortable(Boolean.TRUE)
                .setSortProperty("isDeleted")
                .setAutoWidth(true)
                .setFlexGrow(0);

        productGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        productGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        productGrid.asMultiSelect().addSelectionListener(event -> {
            selectedProducts.clear();
            selectedProducts.addAll(event.getAllSelectedItems());
        });

        productSearchField = new TextField();
        productSearchField.setWidth("50%");
        productSearchField.setPlaceholder("Search");
        productSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        productSearchField.setValueChangeMode(ValueChangeMode.EAGER);
        productSearchField.addValueChangeListener(e -> productGrid.setItems(query -> searchDataProvider.apply(query, e.getValue())));
        productSearchField.setWidth("25%");


        Binder<Product> productBinder = new BeanValidationBinder<>(Product.class);
        Editor<Product> productEditor = productGrid.getEditor();
        productEditor.setBinder(productBinder);


        TextField code = new TextField();
        code.setRequired(true);
        code.setWidthFull();
        code.setErrorMessage("Product code is mandatory");
        code.setRequiredIndicatorVisible(true);
        codeCol.setEditorComponent(code);

        productBinder.forField(code).bind(Product::getCode, Product::setCode);
        productGrid.addItemDoubleClickListener(e -> {
            productEditor.editItem(e.getItem());
            Component editorComponent = e.getColumn().getEditorComponent();
            if (editorComponent instanceof Focusable focusable) {
                focusable.focus();
            }
        });

    }


    void doArchiveProduct(Set<Product> products) {
        Set<Product> deleted = products.stream().map(p -> p.setDeleted(Boolean.TRUE)).collect(Collectors.toSet());
        productService.saveBatch(deleted);
        productGrid.getDataProvider().refreshAll();
        productGrid.deselectAll();
    }

    void doPublishProduct(Set<Product> products) {
        Set<Product> published = products.stream().map(p -> p.setDeleted(Boolean.FALSE)).collect(Collectors.toSet());
        productService.saveBatch(published);
        productGrid.getDataProvider().refreshAll();
        productGrid.deselectAll();
    }

    boolean doSaveProduct(Product product) {
        try {
            productService.save(product);
            NotificationUtil.showSuccess("Product Added!");
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                NotificationUtil.showError("Duplicate or Inconsistent error Data occurs!");
                return false;
            }
            NotificationUtil.showError(Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("Oops something went wrong!"));
            return false;
        }

        return true;
    }

    boolean checkIsValid(HasValidation... fields) {
        boolean isValid = true;
        for (HasValidation field : fields) {
            isValid &= !field.isInvalid();
        }
        return isValid;
    }
}
