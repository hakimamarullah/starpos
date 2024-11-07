package com.starline.components;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/7/2024 1:13 PM
@Last Modified 11/7/2024 1:13 PM
Version 1.0
*/

import com.starline.constants.StyleProps;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class Container extends HorizontalLayout {

    private final NativeLabel caption;
    private final HorizontalLayout layout;


    public Container() {
        this.caption = new NativeLabel();
        this.layout = new HorizontalLayout();
        layout.getStyle().set(StyleProps.BORDER, "1px solid gray");
        layout.getStyle().set("padding", "15px");
        layout.setWidth("100%");
        layout.setHeight("100%");
        add(caption, layout);
        this.getStyle().setOverflow(Style.Overflow.AUTO);
        this.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
        this.getStyle().setAlignItems(Style.AlignItems.START);
        this.getStyle().setFlexDirection(Style.FlexDirection.COLUMN);
        this.setWidth("100%");
        this.setHeight("100%");
    }

    public void addContent(Component ...components) {
        this.layout.add(components);
    }

    public void setCaption(String caption) {
        this.caption.setText(caption);
    }

    public HorizontalLayout getContainer() {
        return this.layout;
    }

    public Style getContainerStyle() {
        return this.layout.getStyle();
    }


}
