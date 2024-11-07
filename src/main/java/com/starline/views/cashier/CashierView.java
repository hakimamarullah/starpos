package com.starline.views.cashier;

import com.starline.constants.StyleProps;
import com.starline.data.dto.Item;
import com.starline.utils.CommonUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wontlost.zxing.Constants;
import com.wontlost.zxing.ZXingVaadinReader;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@PageTitle("Cashier")
@Route("cashier")
@JsModule("./vaadin-zxing-reader.js")
@Menu(order = 0, icon = "line-awesome/svg/cash-register-solid.svg")
@RolesAllowed("USER")
public class CashierView extends Composite<VerticalLayout> {

    private static final Logger log = LoggerFactory.getLogger(CashierView.class);

    private final transient List<Item> itemList;

    private final ListDataProvider<Item> listItemProvider;
    private final NumberField totalPriceField = new NumberField();
    private final NumberField totalDiscountField = new NumberField();

    private final NumberField taxField = new NumberField();

    private final NumberField grandTotalField = new NumberField();

    public CashierView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.setWidth("100%");
        layoutRow.setHeight("max-content");
        layoutRow.getStyle().set(StyleProps.BORDER, "1px solid red");
        layoutRow.getStyle().setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);

        VerticalLayout itemsLayout = new VerticalLayout();
        itemsLayout.setWidth("100%");
        itemsLayout.setHeight("100%");
        itemsLayout.getStyle().set(StyleProps.FLEX_GROW, "1");
        itemsLayout.getStyle().set(StyleProps.BORDER, "1px solid gray");

        VerticalLayout detailPrice = new VerticalLayout();
        detailPrice.setWidth("100%");
        detailPrice.getStyle().set(StyleProps.FLEX_GROW, "1");
        detailPrice.getStyle().set(StyleProps.BORDER, "1px solid gray");

        layoutRow.add(itemsLayout);
        layoutRow.add(detailPrice);

        itemList = new ArrayList<>();
        listItemProvider = new ListDataProvider<>(itemList);


        itemsLayout.add(initItemList());
        detailPrice.add(initDetailCalculation());

        Dialog dialog = initFormAddItem();
        Button button = new Button("Add Item", e -> dialog.open());
        HorizontalLayout layoutButton = new HorizontalLayout();
        layoutButton.setWidth("100%");
        layoutButton.add(button);


        HorizontalLayout qrContainer = new HorizontalLayout();
        qrContainer.setWidth("100%");
        qrContainer.getStyle().setHeight("100px");
        qrContainer.getStyle().setAlignItems(Style.AlignItems.CENTER);
        qrContainer.getStyle().setOverflow(Style.Overflow.HIDDEN);
        qrContainer.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
        qrContainer.add(getzXingVaadinReader());

        getContent().add(qrContainer);
        getContent().add(dialog);
        getContent().add(layoutButton);
        getContent().add(layoutRow);

    }

    ZXingVaadinReader getzXingVaadinReader() {
        ZXingVaadinReader zxingReader = new ZXingVaadinReader();
        zxingReader.setFrom(Constants.From.camera);
        zxingReader.setWidth("350");
        zxingReader.setId("video");
        zxingReader.setStyle("border: 1px solid gray");
        zxingReader.addValueChangeListener(e ->
        {
            log.info("====================================================");
            log.info("=================QR=====: {}", e.getValue());
            log.info("====================================================");
            zxingReader.reset();

        });
        return zxingReader;
    }

    Grid<Item> initItemList() {
        Grid<Item> itemGrid = new Grid<>(Item.class, false);
        itemGrid.setAllRowsVisible(true);
        itemGrid.setDataProvider(listItemProvider);

        Function<Item, String> calcSubtotal = item -> CommonUtil.formatRupiah(item.getPrice() * item.getQuantity());
        itemGrid.addColumn(Item::getCode).setHeader("Code");

        itemGrid.addColumn(Item::getName).setHeader("Name").setAutoWidth(true).setFlexGrow(0).getStyle().set("font-weight", "800");
        itemGrid.addColumn(Item::getPrice).setHeader("Price");
        itemGrid.addColumn(Item::getQuantity).setHeader("Qty");
        itemGrid.addColumn(calcSubtotal::apply).setHeader("Subtotal");


        return itemGrid;
    }

    VerticalLayout initDetailCalculation() {
        VerticalLayout detailCalculationSection = new VerticalLayout();
        detailCalculationSection.setWidth("100%");

        HorizontalLayout totalLayout = new HorizontalLayout();
        totalLayout.setWidth("100%");
        NativeLabel totalValue = new NativeLabel(totalPriceField.getOptionalValue().map(CommonUtil::formatRupiah).orElse("0"));
        totalLayout.add(new NativeLabel("Total: "));
        totalLayout.add(totalValue);

        HorizontalLayout discountLayout = new HorizontalLayout();
        discountLayout.setWidth("100%");
        NativeLabel discountValue = new NativeLabel(totalDiscountField.getOptionalValue().map(CommonUtil::formatRupiah).orElse("0"));
        discountLayout.add(new NativeLabel("Discount: "));
        discountLayout.add(discountValue);

        HorizontalLayout taxLayout = new HorizontalLayout();
        taxLayout.setWidth("100%");
        NativeLabel taxValue = new NativeLabel(taxField.getOptionalValue().map(CommonUtil::formatRupiah).orElse("0"));
        taxLayout.add(new NativeLabel("Tax: "));
        taxLayout.add(taxValue);

        HorizontalLayout grandTotalLayout = new HorizontalLayout();
        grandTotalLayout.setWidth("100%");
        NativeLabel grandTotalValue = new NativeLabel(grandTotalField.getOptionalValue().map(CommonUtil::formatRupiah).orElse("0"));
        grandTotalLayout.add(new NativeLabel("Grand Total: "));
        grandTotalLayout.add(grandTotalValue);


        detailCalculationSection.add(totalLayout);
        detailCalculationSection.add(discountLayout);
        detailCalculationSection.add(taxLayout);
        detailCalculationSection.add(grandTotalLayout);

        totalPriceField.addValueChangeListener(event -> totalValue.setText(CommonUtil.formatRupiah(event.getValue())));
        totalDiscountField.addValueChangeListener(event -> discountValue.setText(CommonUtil.formatRupiah(event.getValue())));
        taxField.addValueChangeListener(event -> taxValue.setText(CommonUtil.formatRupiah(event.getValue())));
        grandTotalField.addValueChangeListener(event -> grandTotalValue.setText(CommonUtil.formatRupiah(event.getValue())));
        return detailCalculationSection;

    }

    Dialog initFormAddItem() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add Item");
        dialog.setCloseOnEsc(true);
        dialog.setWidth("400px");

        FormLayout formLayout = new FormLayout();


        TextField code = new TextField();
        code.setRequired(true);
        code.setErrorMessage("Product code is mandatory");
        code.setRequiredIndicatorVisible(true);

        TextField name = new TextField();
        name.setRequired(true);
        name.setRequiredIndicatorVisible(true);

        NumberField price = new NumberField();
        price.setRequiredIndicatorVisible(true);
        price.setMin(0.0);
        price.setErrorMessage("Price can't be smaller than 0");
        price.setRequired(true);

        IntegerField quantity = new IntegerField();
        quantity.setMin(1);
        quantity.setErrorMessage("Quantity must be greater than 0");
        quantity.setAutocomplete(Autocomplete.ON);
        quantity.setRequiredIndicatorVisible(true);
        quantity.setRequired(true);


        formLayout.addFormItem(code, "Code");
        formLayout.addFormItem(name, "Name");
        formLayout.addFormItem(price, "Price");
        formLayout.addFormItem(quantity, "Quantity");


        dialog.add(formLayout);

        Button submit = new Button("Add");
        submit.addClickListener(e -> {
            Item item = new Item();
            item.setCode(code.getValue());
            item.setName(name.getValue());
            item.setPrice(price.getValue());
            item.setQuantity(quantity.getOptionalValue().orElse(0));

            if (Boolean.FALSE.equals(checkIsValid(code, name, price, quantity))) {
                return;
            }
            addItem(item);
            dialog.close();
            code.setValue("");
            name.setValue("");
            price.setValue(0.0);
            quantity.setValue(0);
        });

        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancel, submit);


        return dialog;
    }

    void addItem(Item item) {
        itemList.add(item);
        double totalPrice = itemList.stream().mapToDouble(val -> val.getPrice() * val.getQuantity()).sum();
        double tax = totalPrice * 0.1;
        double grandTotal = totalPrice + tax;
        listItemProvider.refreshAll();

        totalPriceField.setValue(totalPrice);
        taxField.setValue(tax);
        grandTotalField.setValue(grandTotal);
    }

    boolean checkIsValid(HasValidation... fields) {
        boolean isValid = true;
        for (HasValidation field : fields) {
            isValid &= !field.isInvalid();
        }
        return isValid;
    }
}