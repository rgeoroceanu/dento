package com.company.dento.ui.component.common;

import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class FilterDialog extends Dialog implements Localizable {

    private final FormLayout layout;
    private final Div filterLabel;
    private final Button filterButton;

    public FilterDialog() {
        final Div content = new Div();
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        this.layout = new FormLayout();
        this.layout.addClassName("dento-filter-dialog");
        this.layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        this.filterLabel = new Div();
        this.filterLabel.addClassName("dento-confirm-dialog-header");
        this.filterButton = new Button();
        this.filterButton.addClassName("dento-button-full");
        buttonsLayout.add(filterButton);
        content.add(filterLabel, layout, buttonsLayout);
        buttonsLayout.setMargin(true);
        buttonsLayout.addClassName("dento-dialog-buttons");
        this.add(content);
    }

    public void addFilter(final String label, final Component filter) {
        this.layout.addFormItem(filter, label);
        ((HasStyle) filter).addClassName("dento-filter-dialog-field");
    }

    public void addApplyFilterListener(final ComponentEventListener<ClickEvent<Button>> listener) {
        filterButton.addClickListener(listener);
        filterButton.addClickListener(e -> this.close());
    }

    @Override
    public void localize() {
        filterButton.setText(Localizer.getLocalizedString("filter"));
        filterLabel.setText(Localizer.getLocalizedString("search"));
    }
}
