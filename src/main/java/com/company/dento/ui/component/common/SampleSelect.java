package com.company.dento.ui.component.common;

import com.company.dento.model.business.Sample;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
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

public class SampleSelect extends AbstractCompositeField<VerticalLayout, SampleSelect, List<Sample>> implements Localizable {

    private final List<Sample> value = new ArrayList<>();
    private final Grid<Sample> grid;

    public SampleSelect() {
        super(null);

        grid = new Grid<>(Sample.class);
        grid.getElement().setAttribute("theme", "row-stripes");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("job.template.name").setWidth("18em").setSortable(false);
        grid.addColumn("template.name").setWidth("18em").setSortable(false);
        grid.addComponentColumn(this::addDatePickerColumn).setKey("date").setWidth("12em").setSortable(false);
        grid.addComponentColumn(this::addTimePickerColumn).setKey("time").setWidth("17em").setSortable(false);
        grid.addClassName("dento-grid");

        this.getContent().add(grid);
        this.getContent().setPadding(false);
        this.getContent().getStyle().set("margin-bottom", "15px");
        this.getContent().setHeight("20em");
        this.getContent().setWidth("54em");
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("job"));
        grid.getColumns().get(1).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("date").setHeader(Localizer.getLocalizedString("date"));
        grid.getColumnByKey("time").setHeader(Localizer.getLocalizedString("time"));
    }

    @Override
    public void setPresentationValue(final List<Sample> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
    }

    public List<Sample> getValue() {
        return new ArrayList<>(value);
    }

    private DatePicker addDatePickerColumn(final Sample sample) {
        final DatePicker datePicker = new DatePicker();
        datePicker.setValue(sample.getDate());
        datePicker.addValueChangeListener(e -> sample.setDate(e.getValue()));
        datePicker.setWidth("10em");
        datePicker.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        return datePicker;
    }

    private TimePicker addTimePickerColumn(final Sample sample) {
        final TimePicker timePicker = new TimePicker();
        timePicker.setValue(sample.getTime());
        timePicker.addValueChangeListener(e -> sample.setTime(e.getValue()));
        return timePicker;
    }
}
