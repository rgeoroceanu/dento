package com.company.dento.ui.component.common;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class TimePicker extends AbstractCompositeField<HorizontalLayout, TimePicker, LocalTime> {

    private static final List<Integer> MINUTES = Arrays.asList(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
    private final ComboBox<Integer> hour;
    private final ComboBox<Integer> minutes;

    public TimePicker() {
        super(null);
        hour = new ComboBox<>();
        hour.setItems(IntStream.range(0, 24).boxed());
        hour.setPreventInvalidInput(true);
        hour.setAllowCustomValue(false);
        hour.addClassName("dento-grid-filter-small");
        minutes = new ComboBox<>();
        minutes.setItems(MINUTES);
        minutes.setPreventInvalidInput(true);
        minutes.setAllowCustomValue(false);
        minutes.addClassName("dento-grid-filter-small");
        final Label delimiter = new Label("  :  ");
        getContent().add(hour, delimiter, minutes);
        hour.setWidth("6em");
        minutes.setWidth("6em");
        hour.addValueChangeListener(e -> changeValue());
        minutes.addValueChangeListener(e -> changeValue());

        getContent().getStyle().set("align-items", "center");
        getContent().setSpacing(false);
        getContent().setPadding(false);
    }

    @Override
    protected void setPresentationValue(final LocalTime localTime) {
        if (localTime == null) {
            hour.clear();
            minutes.clear();
        } else {
            hour.setValue(localTime.getHour());
            minutes.setValue(findClosestMinute(localTime.getMinute()));
        }
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        minutes.setRequiredIndicatorVisible(required);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return minutes.isRequiredIndicatorVisible();
    }

    private void changeValue() {
        if (hour.getOptionalValue().isPresent() && minutes.getOptionalValue().isPresent()) {
            final int minutesValue = minutes.getValue();
            final int hourValue = hour.getValue();
            setModelValue(LocalTime.of(hourValue, minutesValue), true);
        } else {
            setModelValue(null, true);
        }
    }

    private int findClosestMinute(final int minute) {
        return MINUTES.stream()
                .map(minutes -> minutes - minute)
                .min(Integer::compareTo)
                .map(f -> f + minute)
                .orElse(0);
    }
}
