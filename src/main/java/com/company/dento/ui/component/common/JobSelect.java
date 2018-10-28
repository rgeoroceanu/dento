package com.company.dento.ui.component.common;

import com.company.dento.model.business.JobTemplate;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

    public class JobSelect extends VerticalLayout {

    private final ComboBox<JobTemplate> jobSelect;
    private final List<JobTemplate> value = new ArrayList<>();
    private final Grid<JobTemplate> grid;

    public JobSelect() {
        final HorizontalLayout buttons = new HorizontalLayout();

        grid = new Grid<>(JobTemplate.class);
        grid.getElement().setAttribute("theme", "row-stripes");
        grid.addClassName("dento-noheader-grid");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("name").setWidth("67%").setSortable(false);
        grid.addComponentColumn(jb -> {
            final Button removeButton = new Button();
            final Icon icon = new Icon(VaadinIcon.TRASH);
            icon.addClassName("dento-grid-icon");
            icon.setSize("1.4em");
            removeButton.addClassName("dento-grid-action");
            removeButton.addClickListener(e -> remove(jb));
            removeButton.setIcon(icon);
            return removeButton;
        });

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple",  "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        jobSelect = new ComboBox<>();
        jobSelect.setWidth("70%");

        buttons.add(jobSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");
        this.add(buttons, grid);
        this.setPadding(false);
    }

    public void setItems(final List<JobTemplate> items) {
        value.clear();
        grid.setItems(value);
        jobSelect.setItems(items);
    }

    public void setValue(final List<JobTemplate> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
    }

    public List<JobTemplate> getValue() {
        return new ArrayList<>(value);
    }

    private void add() {
        jobSelect.getOptionalValue()
                .ifPresent(v -> {
                    value.add(v);
                    grid.setItems(value);
                });
    }

    private void remove(final JobTemplate item) {
        value.remove(item);
        grid.setItems(value);
    }
}
