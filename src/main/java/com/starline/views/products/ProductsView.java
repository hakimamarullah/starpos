package com.starline.views.products;

import com.starline.components.Container;
import com.starline.components.NotificationUtil;
import com.starline.data.Product;
import com.starline.services.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;
import java.util.Optional;

@PageTitle("Products")
@Route("products")
@Menu(order = 1, icon = "line-awesome/svg/product-hunt.svg")
@RolesAllowed("USER")
public class ProductsView extends Composite<VerticalLayout> {

    private static final Logger log = LoggerFactory.getLogger(ProductsView.class);
    private final Container container;

    private final ProductService productService;
    private Grid<Product> productGrid;

    public ProductsView(ProductService productService) {
        this.productService = productService;
        container = new Container();
        container.setCaption("Add Product");
        container.getContainerStyle().setFlexDirection(Style.FlexDirection.COLUMN);

        Dialog formAddProduct = initFormAddProduct();

        Button newProductBtn = new Button("New", e -> formAddProduct.open());
        Button deleteProductBtn = new Button("Delete", e -> log.info("delete"));
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.getStyle().setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);
        btnLayout.getStyle().setAlignItems(Style.AlignItems.START);
        btnLayout.setWidth("30%");
        btnLayout.add(newProductBtn, deleteProductBtn);
        addContent(formAddProduct);
        addContent(btnLayout);
        addContent(initProductGrid());


        productGrid.addSelectionListener(event -> {
            event.getAllSelectedItems().forEach(p -> log.info("{}", p.getId()));
        });

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

    Grid<Product> initProductGrid() {
        productGrid = new Grid<>(Product.class, false);
        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        productGrid.setItems(query -> {
            Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize(), Sort.by(Sort.Direction.DESC, "createdTime"));
            return productService.list(pageable).getContent().stream();
        });

        SerializableBiConsumer<Span, Product> deleteFlagUpdater = (span, product) -> {
            String theme = String.format("badge %s", Boolean.TRUE.equals(product.isDeleted()) ? "error" : "success");
            span.getElement().setAttribute("theme", theme);
            span.setText(Boolean.TRUE.equals(product.isDeleted()) ? "Out-of-Stock" : "In-Stock");
        };
        productGrid.addColumn(Product::getCode).setHeader("Code");
        productGrid.addColumn(Product::getName).setHeader("Name");
        productGrid.addColumn(new NumberRenderer<>(Product::getPrice, Locale.of("id", "ID"))).setHeader("Price");
        productGrid.addColumn(Product::getDescription).setHeader("Description").setAutoWidth(true).setFlexGrow(0);
        productGrid.addColumn(new ComponentRenderer<>(Span::new, deleteFlagUpdater)).setHeader("Status").setAutoWidth(true).setFlexGrow(0);


        return productGrid;
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
