package com.company.dento.ui.component.common;

import com.company.dento.model.business.Base;
import com.company.dento.model.business.Material;
import com.company.dento.model.business.MaterialTemplate;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
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

public class MaterialField extends AbstractCompositeField<VerticalLayout, MaterialField, Set<Material>> implements Localizable {

    private final ComboBox<MaterialTemplate> templateSelect;
    private final Set<Material> value = new HashSet<>();
    private final Grid<Material> grid;
    private List<MaterialTemplate> templates = new ArrayList<>();
    private final Label emptyText = new Label();
    private final FooterRow footer;

    public MaterialField() {
        super(null);

        final HorizontalLayout buttons = new HorizontalLayout();
        grid = new Grid<>(Material.class);
        grid.getElement().setAttribute("theme", "row-stripes no-border");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn(m -> m.getTemplate().getName()).setSortable(false);
        grid.addComponentColumn(this::addQuantityColumn).setKey("quantity").setFlexGrow(0).setWidth("10em").setSortable(false);
        addRemoveColumn();

        grid.addClassName("dento-grid");
        grid.setHeightByRows(true);

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple",  "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        templateSelect = new ComboBox<>();
        templateSelect.setWidth("80%");
        templateSelect.setMaxWidth("320px");
        templateSelect.setItemLabelGenerator(MaterialTemplate::getName);

        buttons.add(templateSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");

        footer = grid.appendFooterRow();
        footer.getCells().get(0).setComponent(emptyText);

        this.getContent().add(buttons, grid);
        this.getContent().setPadding(false);
        this.getContent().setWidth("90%");
        this.getContent().setMinWidth("200px");
        this.getContent().setMaxWidth("650px");
        this.getContent().getStyle().set("margin-bottom", "15px");
        this.getContent().getStyle().set("margin-top", "10px");
        this.addValueChangeListener(e -> updateOptions(e.getValue()));
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("material"));
        grid.getColumnByKey("quantity").setHeader(Localizer.getLocalizedString("quantity"));
        emptyText.setText("Niciun material selectat!");
    }

    public void setTemplates(final List<MaterialTemplate> items) {
        this.templates = items;
        //value.clear();
        //grid.setItems(value);
        templateSelect.setItems(items);
    }

    @Override
    public Set<Material> getValue() {
        return new HashSet<>(value);
    }

    @Override
    protected void setPresentationValue(final Set<Material> materials) {
        this.value.clear();
        this.value.addAll(materials);
        grid.setItems(value);
        emptyText.setVisible(value.isEmpty());
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        templateSelect.setRequiredIndicatorVisible(required);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return templateSelect.isRequiredIndicatorVisible();
    }

    private void updateOptions(final Set<Material> value) {
        final Map<Long, MaterialTemplate> ids = templates.stream()
                .collect(Collectors.toMap(Base::getId, s -> s));

        value.stream()
                .filter(m -> ids.containsKey(m.getTemplate().getId()))
                .forEach(m -> ids.remove(m.getTemplate().getId()));

        templateSelect.setItems(ids.values());
    }

    private void add() {
        templateSelect.getOptionalValue()
                .ifPresent(v -> {
                    final Material i = new Material();
                    i.setTemplate(v);
                    i.setPrice(v.getPricePerUnit());
                    value.add(i);
                    grid.setItems(value);
                    setModelValue(value, true);
                    emptyText.setVisible(value.isEmpty());
                });
    }

    private void remove(final Material item) {
        value.remove(item);
        grid.setItems(value);
        setModelValue(value, true);
        emptyText.setVisible(value.isEmpty());
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
