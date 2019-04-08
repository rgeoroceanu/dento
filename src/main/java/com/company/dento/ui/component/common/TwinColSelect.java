package com.company.dento.ui.component.common;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TwinColSelect<T> extends AbstractCompositeField<HorizontalLayout, TwinColSelect<T>, List<T>> {

    private final Grid<T> options;
    private final Grid<T> value;
    private final List<T> optionItems = new ArrayList<>();
    private final List<T> valueItems = new ArrayList<>();

    public TwinColSelect(final Class<T> itemClass, final String... properties) {
        super(null);

        options = new Grid<>(itemClass);
        value = new Grid<>(itemClass);
        options.setSelectionMode(Grid.SelectionMode.MULTI);
        value.setSelectionMode(Grid.SelectionMode.MULTI);

        options.removeAllColumns();
        value.removeAllColumns();
        options.addColumns(properties);
        value.addColumns(properties);

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.ANGLE_RIGHT));
        addButton.addClickListener(e -> add());
        final Button removeButton = new Button();
        removeButton.setIcon(new Icon(VaadinIcon.ANGLE_LEFT));
        removeButton.addClickListener(e -> remove());

        final VerticalLayout buttonsLayout = new VerticalLayout();
        buttonsLayout.add(addButton, removeButton);

        options.setSizeFull();
        value.setSizeFull();
        buttonsLayout.setWidth("60px");
        this.getContent().add(options, buttonsLayout, value);
        this.getContent().setWidth("90%");
        this.getContent().setHeight("400px");
        this.getContent().setAlignItems(FlexComponent.Alignment.BASELINE);
    }

    public void setOptions(final List<T> options) {
        optionItems.clear();
        valueItems.clear();
        this.options.setItems(Collections.emptyList());
        this.value.setItems(Collections.emptyList());
        optionItems.addAll(options);
        this.options.setItems(options);
    }

    @Override
    protected void setPresentationValue(final List<T> value) {
        valueItems.clear();
        valueItems.addAll(value);
        this.value.setItems(value);
        final List<T> remainingOptions = new ArrayList<>(optionItems);
        remainingOptions.removeAll(value);
        options.setItems(remainingOptions);
    }

    @Override
    public List<T> getValue() {
        return new ArrayList<>(valueItems);
    }

    private void add() {
        final Set<T> toAdd = options.getSelectedItems();
        if (!toAdd.isEmpty()) {
            final List<T> remainingOptions = new ArrayList<>(optionItems);
            remainingOptions.removeAll(valueItems);
            remainingOptions.removeAll(toAdd);
            options.setItems(remainingOptions);
            this.valueItems.addAll(remainingOptions);
            this.value.setItems(valueItems);
        }
    }

    private void remove() {
        final Set<T> toRemove = value.getSelectedItems();
        if (!toRemove.isEmpty()) {
            final List<T> remainingOptions = new ArrayList<>(optionItems);
            remainingOptions.removeAll(valueItems);
            remainingOptions.addAll(toRemove);
            options.setItems(remainingOptions);
            this.valueItems.removeAll(remainingOptions);
            this.value.setItems(valueItems);
        }
    }

}
