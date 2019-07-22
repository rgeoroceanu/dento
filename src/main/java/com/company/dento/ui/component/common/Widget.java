package com.company.dento.ui.component.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class Widget extends Div {

    private final Div contentWrapper = new Div();

    public Widget(final String title) {
        final Div titleDiv = new Div();
        titleDiv.setText(title);
        titleDiv.addClassName("dento-widget-title");
        this.add(titleDiv, contentWrapper);
        contentWrapper.setSizeFull();
        this.addClassName("dento-widget");
    }

    public void setContent(final Component content) {
        this.contentWrapper.add(content);
    }
}