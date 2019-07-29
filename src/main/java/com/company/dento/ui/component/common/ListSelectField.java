package com.company.dento.ui.component.common;

import com.company.dento.model.business.Base;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ListSelectField<T, S extends Base> extends AbstractCompositeField<VerticalLayout, ListSelectField<T, S>, Set<T>> implements Localizable {

    protected final Set<T> value = new HashSet<>();
    private final ComboBox<S> optionsSelect;
    protected final Grid<T> grid;
    private List<S> options = new ArrayList<>();
    private final Label emptyText = new Label("Niciun element!");

    protected ListSelectField(final Class<T> itemClass) {
        super(null);

        final HorizontalLayout buttons = new HorizontalLayout();
        grid = new Grid<>(itemClass);
        grid.getElement().setAttribute("theme", "row-stripes no-border");
        grid.getColumns().forEach(grid::removeColumn);
        addColumns();
        addRemoveColumn();

        grid.addClassName("dento-grid");
        grid.getStyle().set("margin-top", "0px");

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple",  "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        optionsSelect = new ComboBox<>();
        optionsSelect.setWidth("80%");
        optionsSelect.setMaxWidth("450px");
        optionsSelect.setItemLabelGenerator(getOptionsLabelGenerator());

        buttons.add(optionsSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");

        final FooterRow footer = grid.appendFooterRow();
        footer.getCells().get(0).setComponent(emptyText);

        this.getContent().add(buttons, grid);
        this.getContent().setPadding(false);
        this.getContent().setWidth("90%");
        this.getContent().setMinWidth("200px");
        this.getContent().setMaxWidth("500px");
        this.getContent().getStyle().set("margin-bottom", "15px");
        this.getContent().getStyle().set("margin-top", "10px");
        this.addValueChangeListener(e -> updateOptions(e.getValue()));
    }

    public void setOptions(final List<S> items) {
        this.options = items;
        updateOptions(value);
    }

    @Override
    protected boolean valueEquals(Set<T> value1, Set<T> value2) {
        return false;
    }

    @Override
    protected void setPresentationValue(final Set<T> value) {
        this.value.clear();
        this.value.addAll(value);
        setGridItems(value);
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        optionsSelect.setRequiredIndicatorVisible(required);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return optionsSelect.isRequiredIndicatorVisible();
    }

    protected abstract ItemLabelGenerator<S> getOptionsLabelGenerator();
    protected abstract void addColumns();
    protected abstract T createNewItem(final S option);
    protected abstract Long getItemOptionId(final T item);

    private void updateOptions(final Set<T> value) {
        final Map<Long, S> ids = options.stream()
                .collect(Collectors.toMap(Base::getId, s -> s));

        value.stream()
                .filter(m -> ids.containsKey(getItemOptionId(m)))
                .forEach(m -> ids.remove(getItemOptionId(m)));

        optionsSelect.setItems(ids.values());
    }

    private void add() {
        optionsSelect.getOptionalValue()
                .ifPresent(v -> {
                    final T item = createNewItem(v);
                    value.add(item);
                    setGridItems(value);
                    setModelValue(value, true);
                });
    }

    private void remove(final T item) {
        value.remove(item);
        setGridItems(value);
        setModelValue(value, true);
    }

    private void addRemoveColumn() {
        grid.addComponentColumn(jb -> {
            final Button removeButton = new Button();
            final Icon icon = new Icon(VaadinIcon.TRASH);
            icon.addClassName("dento-grid-icon");
            icon.setSize("1.4em");
            removeButton.addClassName("dento-grid-action");
            removeButton.addClickListener(e -> remove(jb));
            removeButton.setIcon(icon);
            return removeButton;
        }).setFlexGrow(0);
    }

    private void setGridItems(final Set<T> items) {
        grid.setItems(items);
        emptyText.setVisible(items.isEmpty());
        if (items.size() < 6) {
            grid.setHeightByRows(true);
        } else {
            grid.setHeight("380px");
        }

    }
}
