package com.starline.views.products;

import com.starline.constants.StyleProps;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Products")
@Route("products")
@Menu(order = 1, icon = "line-awesome/svg/product-hunt.svg")
@RolesAllowed("USER")
public class ProductsView extends Composite<VerticalLayout> {

    public ProductsView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set(StyleProps.FLEX_GROW, "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set(StyleProps.FLEX_GROW, "1");
        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
    }
}
