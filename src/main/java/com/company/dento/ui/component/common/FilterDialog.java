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
import com.vaadin.flow.component.html.Label;

public class FilterDialog extends Dialog implements Localizable {

    private final FormLayout layout;
    private final Label filterLabel;
    private final Button filterButton;

    public FilterDialog() {
        this.layout = new FormLayout();
        this.layout.addClassName("dento-filter-dialog");
        this.layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        this.filterLabel = new Label();
        this.filterButton = new Button();

        this.add(filterLabel, layout, filterButton);
    }

    public void addFilter(final String label, final Component filter) {
        this.layout.addFormItem(filter, label);
        ((HasStyle) filter).addClassName("dento-filter-dialog-field");
    }

    public void addApplyFilterListener(final ComponentEventListener<ClickEvent<Button>> listener) {
        filterButton.addClickListener(listener);
    }

    @Override
    public void localize() {
        filterButton.setText(Localizer.getLocalizedString("filter"));
        filterLabel.setText(Localizer.getLocalizedString("search"));
    }
}
