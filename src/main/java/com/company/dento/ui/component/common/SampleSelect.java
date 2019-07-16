package com.company.dento.ui.component.common;

import com.company.dento.model.business.Sample;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SampleSelect extends AbstractCompositeField<VerticalLayout, SampleSelect, Set<Sample>> implements Localizable {

    private final List<Sample> value = new ArrayList<>();
    private final Grid<Sample> grid;
    private final Label emptyText = new Label();

    public SampleSelect() {
        super(null);

        grid = new Grid<>(Sample.class);
        grid.getElement().setAttribute("theme", "row-stripes no-border");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("template.name").setSortable(false);
        grid.addComponentColumn(this::addDatePickerColumn).setKey("date").setFlexGrow(0).setWidth("10em").setSortable(false);
        grid.addComponentColumn(this::addTimePickerColumn).setKey("time").setFlexGrow(0).setWidth("8em").setSortable(false);
        grid.addClassName("dento-grid");
        grid.setHeightByRows(true);
        final FooterRow footer = grid.appendFooterRow();
        footer.getCells().get(0).setComponent(emptyText);

        this.getContent().add(grid);
        this.getContent().setPadding(false);
        this.getContent().getStyle().set("margin-bottom", "15px");
        this.getContent().getStyle().set("margin-top", "10px");
        this.getContent().setMaxHeight("20em");
        this.getContent().setWidth("90%");
        this.getContent().getStyle().set("min-width", "200px");
        this.getContent().getStyle().set("max-width", "650px");
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("date").setHeader(Localizer.getLocalizedString("date"));
        grid.getColumnByKey("time").setHeader(Localizer.getLocalizedString("time"));
        emptyText.setText(Localizer.getLocalizedString("emptySamples"));
    }

    @Override
    public void setPresentationValue(final Set<Sample> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
        emptyText.setVisible(value.isEmpty());
    }

    public Set<Sample> getValue() {
        return new HashSet<>(value);
    }

    private DatePicker addDatePickerColumn(final Sample sample) {
        final DatePicker datePicker = new DatePicker();
        datePicker.setValue(sample.getDate());
        datePicker.addValueChangeListener(e -> sample.setDate(e.getValue()));
        datePicker.setWidth("100%");
        datePicker.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        return datePicker;
    }

    private TimePicker addTimePickerColumn(final Sample sample) {
        final TimePicker timePicker = new TimePicker();
        timePicker.setValue(sample.getTime());
        timePicker.addValueChangeListener(e -> sample.setTime(e.getValue()));
        timePicker.addClassName("dento-grid-filter-small");
        timePicker.setWidth("100%");
        return timePicker;
    }
}
