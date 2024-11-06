package com.starline.views.mainview;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("MainView")
@Route("")
@Menu(order = 0, icon = "line-awesome/svg/cash-register-solid.svg")
@RolesAllowed("USER")
public class MainViewView extends Composite<VerticalLayout> {

    public MainViewView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        VerticalLayout layoutColumn4 = new VerticalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn4.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutColumn4);
        layoutColumn4.setWidth("100%");
        layoutColumn4.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(layoutColumn4);
    }
}
