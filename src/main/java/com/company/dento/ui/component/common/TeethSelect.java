package com.company.dento.ui.component.common;

import com.company.dento.model.business.Tooth;
import com.company.dento.model.business.ToothOption;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.TextRenderer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeethSelect extends AbstractCompositeField<Div, TeethSelect, Set<Tooth>> {

    private final Map<Integer, ToothItem> items = new HashMap<>();

    public TeethSelect() {
        super(null);

        this.getContent().addClassName("dento-teeth-select");
        this.getContent().addClassName("dento-form-field");

        final Div line = new Div();
        line.addClassName("dento-teeth-select-line");
        this.getContent().add(line);

        IntStream.range(11, 19).forEach(this::addTooth);
        IntStream.range(21, 29).forEach(this::addTooth);
        IntStream.range(31, 39).forEach(this::addTooth);
        IntStream.range(41, 49).forEach(this::addTooth);
    }

    public void setTeethOptions(final List<ToothOption> optionsColumn1, final List<ToothOption> optionsColumn2, final boolean group) {
        items.values().forEach(i -> i.setOptions(optionsColumn1, optionsColumn2));
        items.values().forEach(i -> i.setSelectable(!group));

        if (group) {
            final Checkbox topSelect = new Checkbox();
            final Checkbox bottomSelect = new Checkbox();
            topSelect.addValueChangeListener(v -> toggleGroupSelect(v.getValue(), true));
            bottomSelect.addValueChangeListener(v -> toggleGroupSelect(v.getValue(), false));

            topSelect.addClassName("dento-teeth-select-top");
            bottomSelect.addClassName("dento-teeth-select-bottom");
            this.getContent().add(topSelect);
            this.getContent().add(bottomSelect);
        }
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
                .collect(Collectors.toSet());
    }

    public void setDisabledTeeth(final Set<Integer> disabled) {
        items.values().forEach(item -> item.setEnabled(true));
        disabled.stream()
                .filter(items::containsKey)
                .map(items::get)
                .forEach(ti -> ti.setEnabled(false));
    }

    private void toggleGroupSelect(final boolean select, final boolean top) {
        final List<Integer> g1 = IntStream.range(11, 19).boxed().collect(Collectors.toList());
        final List<Integer> g2 = IntStream.range(21, 29).boxed().collect(Collectors.toList());
        final List<Integer> g3 = IntStream.range(31, 39).boxed().collect(Collectors.toList());
        final List<Integer> g4 = IntStream.range(41, 49).boxed().collect(Collectors.toList());

        final List<Integer> group = new ArrayList<>();
        if (top) {
            group.addAll(g1);
            group.addAll(g2);
        } else {
            group.addAll(g3);
            group.addAll(g4);
        }

        items.entrySet().stream()
                .filter(entry -> group.contains(entry.getKey()))
                .filter((entry) -> entry.getValue().isEnabled())
                .forEach(entry -> {
                   if (select) {
                        entry.getValue().setValue(new Tooth(entry.getKey()));
                   } else {
                       entry.getValue().setValue(null);
                   }
                });
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

            select.add(toothPropertySelect);
            toothPropertyDisplay = new ToothPropertyDisplay();
            toothPropertyDisplay.setVisible(false);
            toothPropertyDisplay.addClassNames("dento-teeth-select-properties-display", String.format("tooth%d-elem", toothNumber));
            icon.addClassName("dento-teeth-select-tooth-inactive");
            icon.setSize("1.8em");
            button.setIcon(icon);
            button.addClassNames("dento-teeth-select-tooth-button", String.format("tooth%d-elem", toothNumber));
            text = new Label(String.valueOf(toothNumber));
            this.getContent().add(toothPropertyDisplay, button, text);
            this.getContent().addClassNames(String.format("tooth%d", toothNumber), "dento-teeth-select-tooth");
            text.addClassNames("tooth-text", String.format("tooth%d-elem", toothNumber));

            this.toothNumber = toothNumber;
        }

        public void setOptions(final List<ToothOption> col1, final List<ToothOption> col2) {
            toothPropertySelect.setOptions(col1, col2);
        }

        public void setSelectable(final boolean selectable) {
            if (selectable) {
                toothPropertySelect.addSelectionListener(this::handleSelection);
                button.addClickListener(e -> selectProperties());
            }
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
            if (value != null) toothPropertyDisplay.setValue(value.getOption1(), value.getOption2());
            toothPropertyDisplay.setVisible(true);
            //toothPropertySelect.setVisible(false);
            select.close();
            toothPropertySelect.clear();
        }

        private void updateValue(final ToothOption p1, final ToothOption p2) {
            value.setOption1(p1);
            value.setOption2(p2);
        }

        private void handleSelection(final ToothOption p1, final ToothOption p2) {
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

        public void setEnabled(boolean enabled) {
            if (enabled) {
                icon.removeClassName("dento-teeth-select-tooth-disabled");
                button.removeClassName("dento-teeth-select-tooth-disabled");
                text.removeClassName("dento-teeth-select-tooth-disabled");
            } else {
                icon.addClassName("dento-teeth-select-tooth-disabled");
                button.addClassName("dento-teeth-select-tooth-disabled");
                text.addClassName("dento-teeth-select-tooth-disabled");
            }
            super.setEnabled(enabled);
        }
    }

    private static class ToothPropertySelect extends HorizontalLayout {
        private final RadioButtonGroup<ToothOption> g1;
        private final RadioButtonGroup<ToothOption> g2;
        private SelectionListener selectionListener;
        private final List<ToothOption> col1 = new ArrayList<>();
        private final List<ToothOption> col2 = new ArrayList<>();

        private interface SelectionListener {
            void selected(ToothOption t1, ToothOption t2);
        }

        ToothPropertySelect() {
            g1 = new RadioButtonGroup<>();
            g2 = new RadioButtonGroup<>();
            g1.addClassName("dento-teeth-select-radio-group");
            g2.addClassName("dento-teeth-select-radio-group");
            g1.addValueChangeListener(e -> itemSelected());
            g2.addValueChangeListener(e -> itemSelected());
            g1.setRenderer(new TextRenderer<>(ToothOption::getAbbreviation));
            g2.setRenderer(new TextRenderer<>(ToothOption::getAbbreviation));
            this.add(g1, g2);
            this.addClassName("dento-teeth-select-properties");
        }

        void addSelectionListener(final SelectionListener selectionListener) {
            this.selectionListener = selectionListener;
        }

        void setOptions(final List<ToothOption> col1, final List<ToothOption> col2) {
            this.col1.clear();
            this.col2.clear();
            this.col1.addAll(col1);
            this.col2.addAll(col2);
            g1.setItems(col1);
            g2.setItems(col2);
        }

        void clear() {
            g1.clear();
            g2.clear();
            g1.setItems(col1);
            g2.setItems(col2);
        }

        private void itemSelected() {
            final Optional<ToothOption> type = g1.getOptionalValue();
            final Optional<ToothOption> prop = g2.getOptionalValue();
            if (type.isPresent() && prop.isPresent()) {
                selectionListener.selected(type.get(), prop.get());
            }
        }
    }

    private static class ToothPropertyDisplay extends Div {

        private final Div option1;
        private final Div option2;

        private ToothPropertyDisplay() {
            option1 = new Div();
            option2 = new Div();
            this.add(option1, option2);
        }

        void setValue(final ToothOption o1, final ToothOption o2) {
            if (o1 != null) {
                option1.setText(o1.getAbbreviation());
            }

            if (o2 != null) {
                option2.setText(o2.getAbbreviation());
            }
        }
    }
}
