package com.company.dento.ui.component.common;

import com.company.dento.model.business.Material;
import com.company.dento.model.business.MaterialTemplate;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.validator.FloatRangeValidator;

public class MaterialField extends ListSelectField<Material, MaterialTemplate> {


    public MaterialField() {
        super(Material.class);
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("material"));
        grid.getColumnByKey("quantity").setHeader(Localizer.getLocalizedString("quantity"));
    }

    @Override
    protected ItemLabelGenerator<MaterialTemplate> getOptionsLabelGenerator() {
        return MaterialTemplate::getName;
    }

    @Override
    protected void addColumns() {
        grid.addColumn(m -> m.getTemplate().getName()).setSortable(false);
        grid.addComponentColumn(this::addQuantityColumn).setKey("quantity").setFlexGrow(0).setWidth("10em").setSortable(false);
    }

    @Override
    protected Material createNewItem(final MaterialTemplate option) {
        final Material item = new Material();
        item.setTemplate(option);
        item.setPrice(option.getPricePerUnit());
        return item;
    }

    @Override
    protected Long getItemOptionId(final Material item) {
        return item.getTemplate().getId();
    }

    private Component addQuantityColumn(final Material material) {
        final TextField quantityField = new TextField();
        final Binder<Material> binder = new Binder<>();
        binder.forField(quantityField)
                .withConverter(new StringToFloatConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new FloatRangeValidator("integerRangeValidation", 0f, 100000f))
                .bind(Material::getQuantity, Material::setQuantity);

        binder.setBean(material);
        quantityField.setWidth("5em");
        quantityField.setPreventInvalidInput(true);
        quantityField.addClassName("dento-grid-filter-small");

        final Label measurementUnit = new Label(material.getTemplate().getMeasurementUnit().name());
        measurementUnit.getStyle().set("margin-left", "10px");

        final HorizontalLayout hl = new HorizontalLayout(quantityField, measurementUnit);
        hl.getStyle().set("align-items", "center");

        return hl;
    }
}
