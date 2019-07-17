package com.company.dento.ui.component.common;

import com.company.dento.model.business.Base;
import com.company.dento.model.business.Price;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.validator.FloatRangeValidator;

public class PriceField<T extends Price<S>, S extends Base> extends ListSelectField<T, S> {

    private Class<T> itemClass;

    public PriceField(final Class<T> itemClass) {
        super(itemClass);
        this.itemClass = itemClass;
    }

    @Override
    protected ItemLabelGenerator<S> getOptionsLabelGenerator() {
        return S::toString;
    }

    @Override
    protected void addColumns() {
        grid.addColumn(Price::getKeyName).setSortable(false);
        grid.addComponentColumn(this::addPriceColumn).setKey("price").setFlexGrow(0).setWidth("5em").setSortable(false);
    }

    @Override
    protected T createNewItem(final S option) {
        try {
            final T item = this.itemClass.getDeclaredConstructor().newInstance();
            item.setKey(option);
            return item;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Long getItemOptionId(final T item) {
        return item.getKey().getId();
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

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader("Pentru");
        grid.getColumns().get(1).setHeader("Preț");
    }
}
