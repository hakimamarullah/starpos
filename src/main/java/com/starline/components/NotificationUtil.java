package com.starline.components;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/7/2024 3:39 PM
@Last Modified 11/7/2024 3:39 PM
Version 1.0
*/

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Optional;

public class NotificationUtil {

    private NotificationUtil() {

    }

    public static void showError(String message) {
       show(5000, message, NotificationVariant.LUMO_ERROR);
    }

    public static void showSuccess(String message) {
        show(5000, message, NotificationVariant.LUMO_SUCCESS);
    }

    public static void showWarn(String message) { show (3000, message, NotificationVariant.LUMO_WARNING); }

    private static void show(Integer duration, String message, NotificationVariant variant) {
        Notification notif = new Notification();
        notif.addThemeVariants(variant);
        notif.setDuration(Optional.ofNullable(duration).orElse(0));
        notif.setPosition(Notification.Position.TOP_CENTER);

        Div text = new Div(new Text(message));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> notif.close());

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notif.add(layout);
        notif.open();
    }
}
