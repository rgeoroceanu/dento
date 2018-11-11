package com.company.dento.ui.component.common;

import com.company.dento.model.business.Tooth;
import com.company.dento.model.type.ToothProperty;
import com.company.dento.model.type.ToothType;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.Optional;
import java.util.stream.IntStream;

public class TeethSelect extends Div {

    public TeethSelect() {
        this.addClassName("dento-teeth-select");
        IntStream.range(11, 19).forEach(this::addTooth);
        IntStream.range(21, 29).forEach(this::addTooth);
        IntStream.range(31, 39).forEach(this::addTooth);
        IntStream.range(41, 49).forEach(this::addTooth);
    }

    private void addTooth(int toothNumber) {
        final ToothItem tooth = new ToothItem(toothNumber);
        this.add(tooth);
    }

    private static class ToothItem extends Div {

        private final Button button;
        private final Icon icon;
        private final Label text;
        private boolean active = false;
        private Tooth value;
        private final ToothPropertySelect toothPropertySelect;

        ToothItem(final int toothNumber) {
            button = new Button();
            icon = new Icon(VaadinIcon.TOOTH);
            toothPropertySelect = new ToothPropertySelect();
            toothPropertySelect.addSelectionListener(this::setProperties);
            icon.addClassName("dento-teeth-select-tooth-inactive");
            icon.setSize("1.8em");
            button.setIcon(icon);
            button.addClassNames("dento-teeth-select-tooth-button", String.format("tooth%d-elem", toothNumber));
            button.addClickListener(this::selectProperties);
            text = new Label(String.valueOf(toothNumber));
            this.add(button, text);
            this.addClassNames(String.format("tooth%d", toothNumber), "dento-teeth-select-tooth");
            text.addClassNames("tooth-text", String.format("tooth%d-elem", toothNumber));
            value = new Tooth();
            value.setNumber(toothNumber);
        }

        Tooth getValue() {
            return value;
        }

        void setValue(final Tooth value) {
            this.value = value;
        }

        private void selectProperties(final ClickEvent e) {
            if (active) {
                toggleState();
                value.setType(null);
                value.setProperty(null);
                toothPropertySelect.clear();
            } else {
                toothPropertySelect.open();
            }
        }

        private void setProperties(final ToothType p1, final ToothProperty p2) {
            value.setType(p1);
            value.setProperty(p2);
            toggleState();
            toothPropertySelect.close();
            toothPropertySelect.clear();
        }

        private void toggleState() {
            if (active) {
                icon.removeClassName("dento-teeth-select-tooth-active");
                icon.addClassName("dento-teeth-select-tooth-inactive");
                button.removeClassName("dento-teeth-select-tooth-active");
                button.addClassName("dento-teeth-select-tooth-inactive");
                text.removeClassName("dento-teeth-select-tooth-active");
                text.addClassName("dento-teeth-select-tooth-inactive");
                active = false;
            } else {
                icon.removeClassName("dento-teeth-select-tooth-inactive");
                icon.addClassName("dento-teeth-select-tooth-active");
                button.removeClassName("dento-teeth-select-tooth-inactive");
                button.addClassName("dento-teeth-select-tooth-active");
                text.removeClassName("dento-teeth-select-tooth-inactive");
                text.addClassName("dento-teeth-select-tooth-active");
                active = true;
            }
        }
    }

    private static class ToothPropertySelect extends Dialog {
        private final RadioButtonGroup<ToothType> g1;
        private final RadioButtonGroup<ToothProperty> g2;
        private SelectionListener selectionListener;

        private interface SelectionListener {
            void selected(ToothType t1, ToothProperty t2);
        }

        ToothPropertySelect() {
            final HorizontalLayout hl = new HorizontalLayout();
            g1 = new RadioButtonGroup<>();
            g2 = new RadioButtonGroup<>();
            g1.addClassName("dento-teeth-select-radio-group");
            g2.addClassName("dento-teeth-select-radio-group");
            g1.setItems(ToothType.values());
            g2.setItems(ToothProperty.values());
            g1.addValueChangeListener(e -> itemSelected());
            g2.addValueChangeListener(e -> itemSelected());
            hl.add(g1, g2);
            this.add(hl);
            hl.addClassName("dento-teeth-select-properties");
        }

        void addSelectionListener(final SelectionListener selectionListener) {
            this.selectionListener = selectionListener;
        }

        void clear() {
            g1.clear();
            g2.clear();
            g1.setItems(ToothType.values());
            g2.setItems(ToothProperty.values());
        }

        private void itemSelected() {
            final Optional<ToothType> type = g1.getOptionalValue();
            final Optional<ToothProperty> prop = g2.getOptionalValue();
            if (type.isPresent() && prop.isPresent()) {
                selectionListener.selected(type.get(), prop.get());
            }
        }
    }
}
