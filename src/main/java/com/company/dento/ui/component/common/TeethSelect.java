package com.company.dento.ui.component.common;

import com.company.dento.model.business.Tooth;
import com.company.dento.model.type.ToothProperty;
import com.company.dento.model.type.ToothType;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeethSelect extends AbstractCompositeField<Div, TeethSelect, Set<Tooth>> {

    private final Map<Integer, ToothItem> items = new HashMap<>();

    public TeethSelect() {
        super(null);
        IntStream.range(11, 19).forEach(this::addTooth);
        IntStream.range(21, 29).forEach(this::addTooth);
        IntStream.range(31, 39).forEach(this::addTooth);
        IntStream.range(41, 49).forEach(this::addTooth);

        this.getContent().addClassName("dento-teeth-select");
        this.getContent().addClassName("dento-form-field");
    }

    private void addTooth(int toothNumber) {
        final ToothItem tooth = new ToothItem(toothNumber);
        tooth.addValueChangeListener(e -> this.setModelValue(getValue(), true));
        items.put(toothNumber, tooth);
        this.getContent().add(tooth);
    }

    @Override
    protected void setPresentationValue(final Set<Tooth> teeth) {
        teeth.stream()
                .filter(t -> items.containsKey(t.getNumber()))
                .forEach(tooth -> items.get(tooth.getNumber()).setValue(tooth));
    }

    @Override
    public Set<Tooth> getValue() {
        return items.values().stream()
                .filter(ti -> ti.getOptionalValue().isPresent())
                .map(ToothItem::getValue)
                .filter(ti -> ti.getProperty() != null && ti.getType() != null)
                .collect(Collectors.toSet());
    }


    private static class ToothItem extends AbstractCompositeField<Div, ToothItem, Tooth> {

        private final Button button;
        private final Icon icon;
        private final Label text;
        private final ToothPropertySelect toothPropertySelect;
        private final ToothPropertyDisplay toothPropertyDisplay;
        private final int toothNumber;
        private final Dialog select;

        private boolean active = false;
        private Tooth value;

        ToothItem(final int toothNumber) {
            super(null);

            button = new Button();
            icon = new Icon(VaadinIcon.TOOTH);
            select = new Dialog();
            toothPropertySelect = new ToothPropertySelect();
            toothPropertySelect.addSelectionListener(this::handleSelection);
            select.add(toothPropertySelect);
            toothPropertyDisplay = new ToothPropertyDisplay();
            toothPropertyDisplay.setVisible(false);
            toothPropertyDisplay.addClassNames("dento-teeth-select-properties-display", String.format("tooth%d-elem", toothNumber));
            icon.addClassName("dento-teeth-select-tooth-inactive");
            icon.setSize("1.8em");
            button.setIcon(icon);
            button.addClassNames("dento-teeth-select-tooth-button", String.format("tooth%d-elem", toothNumber));
            button.addClickListener(e -> selectProperties());
            text = new Label(String.valueOf(toothNumber));
            this.getContent().add(toothPropertyDisplay, button, text);
            this.getContent().addClassNames(String.format("tooth%d", toothNumber), "dento-teeth-select-tooth");
            text.addClassNames("tooth-text", String.format("tooth%d-elem", toothNumber));

            this.toothNumber = toothNumber;
        }

        @Override
        public Tooth getValue() {
            return value;
        }

        @Override
        public void setPresentationValue(final Tooth value) {
            this.value = value;
            active = value == null;
            updateDisplay();
        }

        private void selectProperties() {
            if (active) {
                toggleState();
                value = null;
                setModelValue(value, true);
                toothPropertySelect.clear();
                toothPropertyDisplay.setVisible(false);
            } else {
                value = new Tooth();
                value.setNumber(toothNumber);
                //toothPropertySelect.setVisible(true);
                select.open();
            }
        }

        private void updateDisplay() {
            toggleState();
            toothPropertyDisplay.setValue(value.getType(), value.getProperty());
            toothPropertyDisplay.setVisible(true);
            //toothPropertySelect.setVisible(false);
            select.close();
            toothPropertySelect.clear();
        }

        private void updateValue(final ToothType p1, final ToothProperty p2) {
            value.setType(p1);
            value.setProperty(p2);
        }

        private void handleSelection(final ToothType p1, final ToothProperty p2) {
            updateValue(p1, p2);
            updateDisplay();
            setModelValue(value, true);
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

    private static class ToothPropertySelect extends HorizontalLayout {
        private final RadioButtonGroup<ToothType> g1;
        private final RadioButtonGroup<ToothProperty> g2;
        private SelectionListener selectionListener;

        private interface SelectionListener {
            void selected(ToothType t1, ToothProperty t2);
        }

        ToothPropertySelect() {
            g1 = new RadioButtonGroup<>();
            g2 = new RadioButtonGroup<>();
            g1.addClassName("dento-teeth-select-radio-group");
            g2.addClassName("dento-teeth-select-radio-group");
            g1.setItems(ToothType.values());
            g2.setItems(ToothProperty.values());
            g1.addValueChangeListener(e -> itemSelected());
            g2.addValueChangeListener(e -> itemSelected());
            this.add(g1, g2);
            this.addClassName("dento-teeth-select-properties");
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

    private static class ToothPropertyDisplay extends Div {

        private final Div typeDiv;
        private final Div propertyDiv;

        private ToothPropertyDisplay() {
            typeDiv = new Div();
            propertyDiv = new Div();
            this.add(typeDiv, propertyDiv);
        }

        void setValue(final ToothType type, final ToothProperty property) {
            typeDiv.setText(type.toString());
            propertyDiv.setText(property.toString());
        }
    }
}
