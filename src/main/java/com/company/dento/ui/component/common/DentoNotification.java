package com.company.dento.ui.component.common;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;

import java.util.Collections;
import java.util.List;

public class DentoNotification {

    private DentoNotification() { }

    public static void error(final String titleText, final List<String> detailsText) {
        final Notification notification = createNotification(titleText, detailsText);
        notification.open();
    }

    public static void error(final String titleText) {
        final Notification notification = createNotification(titleText, Collections.emptyList());
        notification.open();
    }


    public static void success(final String titleText) {
        final Notification notification = new Notification(titleText);
        notification.setDuration(5000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }

    private static Notification createNotification(final String titleText, final List<String> detailsText) {

        final Div title = new Div();
        final UnorderedList details = new UnorderedList();
        title.setText(titleText);
        detailsText.forEach(d -> {
            final ListItem li = new ListItem(d);
            details.add(li);
        });

        final Notification notification = new Notification(title, details);
        title.getStyle().set("color", "red");
        title.addClassName("dento-notification-title");
        details.addClassName("dento-notification-details");
        notification.setDuration(7000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        return notification;
    }
}
