package com.company.dento.ui.component.common;

import com.company.dento.model.business.Clinic;
import com.company.dento.model.business.JobPrice;
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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

import java.util.*;
import java.util.stream.Collectors;

public class PriceField extends AbstractCompositeField<VerticalLayout, PriceField, Set<JobPrice>> {

    private final ComboBox<Clinic> clinicSelect;
    private final Set<JobPrice> value = new HashSet<>();
    private final Grid<JobPrice> grid;
    private List<Clinic> clinics = new ArrayList<>();

    public PriceField() {
        super(null);

        final HorizontalLayout buttons = new HorizontalLayout();
        grid = new Grid<>(JobPrice.class);
        grid.getElement().setAttribute("theme", "row-stripes no-border");
        grid.addClassName("dento-noheader-grid");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("clinic.name").setSortable(false);
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

        clinicSelect = new ComboBox<>();
        clinicSelect.setWidth("80%");
        clinicSelect.setMaxWidth("320px");
        clinicSelect.setItemLabelGenerator(Clinic::getName);

        buttons.add(clinicSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");

        this.getContent().add(buttons, grid);
        this.getContent().setPadding(false);
        this.getContent().setWidth("90%");
        this.getContent().setMinWidth("200px");
        this.getContent().setMaxWidth("650px");
        this.addValueChangeListener(e -> updateClinics(e.getValue()));
    }

    public void setClinics(final List<Clinic> items) {
        this.clinics = items;
        value.clear();
        grid.setItems(value);
        clinicSelect.setItems(items);
    }

    @Override
    public Set<JobPrice> getValue() {
        return new HashSet<>(value);
    }

    @Override
    protected void setPresentationValue(final Set<JobPrice> prices) {
        this.value.clear();
        this.value.addAll(prices);
        grid.setItems(value);
        final List<Clinic> clinics = clinicSelect.getDataProvider()
                .fetch(new Query<>())
                .collect(Collectors.toList());
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        clinicSelect.setRequiredIndicatorVisible(required);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return clinicSelect.isRequiredIndicatorVisible();
    }

    private void updateClinics(final Set<JobPrice> value) {
        final Map<Long, Clinic> clinicIds =clinics.stream().collect(Collectors.toMap(Clinic::getId, c -> c));
        value.stream()
                .filter(jb -> clinicIds.containsKey(jb.getClinic().getId()))
                .forEach(jb -> clinicIds.remove(jb.getClinic().getId()));

        clinicSelect.setItems(clinicIds.values());
    }

    private void add() {
        clinicSelect.getOptionalValue()
                .ifPresent(v -> {
                    final JobPrice item = new JobPrice();
                    item.setClinic(v);
                    value.add(item);
                    grid.setItems(value);
                    setModelValue(value, true);
                });
    }

    private void remove(final JobPrice item) {
        value.remove(item);
        grid.setItems(value);
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

    private TextField addPriceColumn(final JobPrice jobPrice) {
        final TextField price = new TextField();
        final Binder<JobPrice> binder = new Binder<>();
        binder.forField(price)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("integerRangeValidation", 0, 100000))
                .bind(JobPrice::getPrice, JobPrice::setPrice);

        binder.setBean(jobPrice);
        price.setWidth("15em");
        price.setPreventInvalidInput(true);
        price.addClassName("dento-grid-filter-small");
        return price;
    }
}
