package com.company.dento.ui.component.common;

import com.company.dento.model.business.Sample;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class SampleSelect extends VerticalLayout implements Localizable {

    private final ComboBox<SampleTemplate> sampleSelect;
    private final List<Sample> value = new ArrayList<>();
    private final Grid<Sample> grid;

    public SampleSelect() {
        final HorizontalLayout buttons = new HorizontalLayout();

        grid = new Grid<>(Sample.class);
        grid.getElement().setAttribute("theme", "row-stripes");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("template.name").setWidth("18em").setSortable(false);
        grid.addComponentColumn(i -> addDatePickerColumn(i)).setKey("date").setWidth("12em").setSortable(false);
        grid.addComponentColumn(i -> addTimePickerColumn(i)).setKey("time").setWidth("17em").setSortable(false);
        grid.addComponentColumn(jb -> {
            final Button removeButton = new Button();
            final Icon icon = new Icon(VaadinIcon.TRASH);
            icon.addClassName("dento-grid-icon");
            icon.setSize("1.4em");
            removeButton.addClassName("dento-grid-action");
            removeButton.addClickListener(e -> remove(jb));
            removeButton.setIcon(icon);
            return removeButton;
        }).setWidth("4em");

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple",  "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        sampleSelect = new ComboBox<>();
        sampleSelect.setWidth("18em");

        buttons.add(sampleSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");
        this.add(buttons, grid);
        this.setPadding(false);
    }

    @Override
    public void localize() {

    }

    public void setItems(final List<SampleTemplate> items) {
        value.clear();
        grid.setItems(value);
        sampleSelect.setItems(items);
    }

    public void setValue(final List<Sample> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
    }

    public List<Sample> getValue() {
        return new ArrayList<>(value);
    }

    private void add() {
        sampleSelect.getOptionalValue()
                .ifPresent(v -> {
                    final Sample newSample = new Sample();
                    newSample.setTemplate(v);
                    value.add(newSample);
                    grid.setItems(value);
                });
    }

    private void remove(final Sample item) {
        value.remove(item);
        grid.setItems(value);
    }

    private DatePicker addDatePickerColumn(final Sample sample) {
        final DatePicker datePicker = new DatePicker();
        datePicker.setValue(sample.getDate());
        datePicker.addValueChangeListener(e -> sample.setDate(e.getValue()));
        datePicker.setWidth("10em");
        return datePicker;
    }

    private TimePicker addTimePickerColumn(final Sample sample) {
        final TimePicker timePicker = new TimePicker();
        timePicker.setValue(sample.getTime());
        timePicker.addValueChangeListener(e -> sample.setTime(e.getValue()));
        return timePicker;
    }
}
