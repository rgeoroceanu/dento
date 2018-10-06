package com.company.dento.ui.component.common;


import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog implements Localizable {

    private final Button confirmButton;
    private final Button cancelButton;
    private final Div header;
    private final Div text;

    public ConfirmDialog() {
        final VerticalLayout content = new VerticalLayout();
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        header = new Div();
        header.addClassName("dento-confirm-dialog-header");
        text = new Div();
        text.addClassName("dento-confirm-dialog-text");
        confirmButton = new Button();
        cancelButton = new Button();
        cancelButton.addClickListener(e -> this.close());
        cancelButton.addClassName("dento-button-simple");
        confirmButton.addClickListener(e -> this.close());
        confirmButton.addClassNames("dento-button-full", "dento-confirm-dialog-confirm");
        buttonsLayout.add(cancelButton, confirmButton);
        buttonsLayout.setMargin(true);
        buttonsLayout.setWidth("100%");
        content.add(header, text, buttonsLayout);
        content.setPadding(false);
        this.add(content);
        this.setWidth("20em");
    }

    public void setHeader(final String header) {
        this.header.setText(header);
    }

    public void setText(final String text) {
        this.text.setText(text);
    }

    public void addConfirmListener(ComponentEventListener<ClickEvent<Button>> listener) {
        confirmButton.addClickListener(listener);
    }

    @Override
    public void localize() {
        confirmButton.setText(Localizer.getLocalizedString("remove"));
        cancelButton.setText(Localizer.getLocalizedString("cancel"));
    }
}
