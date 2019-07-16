package com.company.dento.ui.component.common;

import com.company.dento.model.business.Base;
import com.company.dento.model.business.Price;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.validator.FloatRangeValidator;

import java.util.*;
import java.util.stream.Collectors;

public class PriceField<T extends Price<S>, S extends Base> extends AbstractCompositeField<VerticalLayout, PriceField<T, S>, Set<T>> {

    private final ComboBox<S> optionSelect;
    private final Set<T> value = new HashSet<>();
    private final Grid<T> grid;
    private List<S> options = new ArrayList<>();
    private final Class<T> itemClass;

    public PriceField(final Class<T> itemClass) {
        super(null);
        this.itemClass = itemClass;

        final HorizontalLayout buttons = new HorizontalLayout();
        grid = new Grid<>(itemClass);
        grid.getElement().setAttribute("theme", "row-stripes no-border");
        grid.addClassName("dento-noheader-grid");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn(Price::getKeyName).setSortable(false);
        grid.addComponentColumn(this::addPriceColumn).setKey("price").setFlexGrow(0).setWidth("5em").setSortable(false);
        addRemoveColumn();

        grid.addClassName("dento-grid");
        grid.setHeightByRows(true);
        grid.setWidthFull();
        grid.setMaxHeight("20em");
        grid.setMaxWidth("400px");

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple",  "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        optionSelect = new ComboBox<>();
        optionSelect.setWidth("80%");
        optionSelect.setMaxWidth("320px");

        buttons.add(optionSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");

        this.getContent().add(buttons, grid);
        this.getContent().setPadding(false);
        this.getContent().setWidth("90%");
        this.getContent().setMinWidth("200px");
        this.getContent().setMaxWidth("650px");
        this.addValueChangeListener(e -> updateOptions(e.getValue()));
    }

    public void setOptions(final List<S> items) {
        this.options = items;
        value.clear();
        grid.setItems(value);
        optionSelect.setItems(items);
    }

    @Override
    public Set<T> getValue() {
        return new HashSet<T>(value);
    }

    @Override
    protected void setPresentationValue(final Set<T> prices) {
        this.value.clear();
        this.value.addAll(prices);
        grid.setItems(value);
        //final List<S> clinics = optionSelect.getDataProvider()
        //        .fetch(new Query<>())
        //        .collect(Collectors.toList());
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        optionSelect.setRequiredIndicatorVisible(required);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return optionSelect.isRequiredIndicatorVisible();
    }

    private void updateOptions(final Set<T> value) {
        final Map<Long, S> ids = options.stream()
                .collect(Collectors.toMap(Base::getId, s -> s));

        value.stream()
                .filter(jb -> ids.containsKey(jb.getKey().getId()))
                .forEach(jb -> ids.remove(jb.getKey().getId()));

        optionSelect.setItems(ids.values());
    }

    private void add() {
        optionSelect.getOptionalValue()
                .ifPresent(v -> {
                    createItem()
                            .ifPresent(i -> {
                                i.setKey(v);
                                value.add(i);
                                grid.setItems(value);
                                setModelValue(value, true);
                            });
                });
    }

    private void remove(final T item) {
        value.remove(item);
        grid.setItems(value);
        setModelValue(value, true);
    }

    private Optional<T> createItem() {
        try {
            return Optional.of(this.itemClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
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

    private TextField addPriceColumn(final T price) {
        final TextField priceField = new TextField();
        final Binder<T> binder = new Binder<>();
        binder.forField(priceField)
                .withConverter(new StringToFloatConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new FloatRangeValidator("integerRangeValidation", 0f, 100000f))
                .bind(T::getPrice, T::setPrice);

        binder.setBean(price);
        priceField.setWidth("15em");
        priceField.setPreventInvalidInput(true);
        priceField.addClassName("dento-grid-filter-small");
        return priceField;
    }
}
