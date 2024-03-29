package com.company.dento.ui.component.common;

import com.company.dento.model.business.Sample;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;

public class SampleSelect extends ListSelectField<Sample, SampleTemplate> {

    public SampleSelect() {
        super(Sample.class);
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("date").setHeader(Localizer.getLocalizedString("date"));
        grid.getColumnByKey("time").setHeader(Localizer.getLocalizedString("time"));
    }

    @Override
    protected ItemLabelGenerator<SampleTemplate> getOptionsLabelGenerator() {
        return SampleTemplate::getName;
    }

    @Override
    protected void addColumns() {
        grid.addColumn("template.name").setSortable(false);
        grid.addComponentColumn(this::addDatePickerColumn).setKey("date").setFlexGrow(0).setWidth("10em").setSortable(false);
        grid.addComponentColumn(this::addTimePickerColumn).setKey("time").setFlexGrow(0).setWidth("8em").setSortable(false);
    }

    @Override
    protected Sample createNewItem(SampleTemplate option) {
        final Sample item = new Sample();
        item.setTemplate(option);
        return item;
    }

    @Override
    protected Long getItemOptionId(final Sample item) {
        return item.getTemplate().getId();
    }

    private DatePicker addDatePickerColumn(final Sample sample) {
        final DatePicker datePicker = new DatePicker();
        datePicker.addValueChangeListener(e -> {
            if (e.isFromClient()) sample.setDate(e.getValue());
        });
        datePicker.setWidth("100%");
        datePicker.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        datePicker.addValueChangeListener(v -> {
            if (v.isFromClient()) setModelValue(value, true);
        });
        datePicker.setLocale(Localizer.getCurrentLocale());
        datePicker.setValue(sample.getDate());
        return datePicker;
    }

    private TimePicker addTimePickerColumn(final Sample sample) {
        final TimePicker timePicker = new TimePicker();
        timePicker.addValueChangeListener(e -> {
            if (e.isFromClient()) sample.setTime(e.getValue());
        });
        timePicker.addClassName("dento-grid-filter-small");
        timePicker.setWidth("100%");
        timePicker.addValueChangeListener(v -> {
            if (v.isFromClient()) setModelValue(value, true);
        });
        timePicker.setLocale(Localizer.getCurrentLocale());
        UI.getCurrent().access(() -> timePicker.setValue(sample.getTime()));
        return timePicker;
    }
}
