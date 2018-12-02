package com.company.dento.ui.component.common;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class ExecutionSelect extends VerticalLayout implements Localizable {

    private final ComboBox<ExecutionTemplate> executionSelect;
    private final List<Execution> value = new ArrayList<>();
    private final Grid<Execution> grid;
    private final List<User> technicians = new ArrayList<>();
    private final DataService dataService;

    public ExecutionSelect(final DataService dataService) {
        this.dataService = dataService;

        final HorizontalLayout buttons = new HorizontalLayout();

        grid = new Grid<>(Execution.class);
        grid.getElement().setAttribute("theme", "row-stripes");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("template.name").setWidth("18em").setSortable(false);
        grid.addComponentColumn(this::addTechnicianColumn).setKey("technician").setWidth("12em").setSortable(false);

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

        executionSelect = new ComboBox<>();
        executionSelect.setWidth("18em");
        executionSelect.setPreventInvalidInput(true);
        executionSelect.setAllowCustomValue(false);
        executionSelect.setItemLabelGenerator(ExecutionTemplate::getName);

        buttons.add(executionSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");

        this.add(buttons, grid);
        this.setPadding(false);
        this.getStyle().set("margin-bottom", "15px");
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("technician").setHeader(Localizer.getLocalizedString("technician"));
    }

    public void setItems(final List<ExecutionTemplate> items) {
        value.clear();
        technicians.clear();
        grid.setItems(value);
        executionSelect.setItems(items);
        technicians.addAll(dataService.getAll(User.class));
    }

    public void setValue(final List<Execution> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
    }

    public List<Execution> getValue() {
        return new ArrayList<>(value);
    }

    private void add() {
        executionSelect.getOptionalValue()
                .ifPresent(v -> {
                    final Execution newExecution = new Execution();
                    newExecution.setTemplate(v);
                    value.add(newExecution);
                    grid.setItems(value);
                });
    }

    private void remove(final Execution item) {
        value.remove(item);
        grid.setItems(value);
    }

    private ComboBox<User> addTechnicianColumn(final Execution execution) {
        final ComboBox<User> technician = new ComboBox<>();
        technician.setItems(technicians);
        technician.setValue(execution.getTechnician());
        technician.addValueChangeListener(e -> execution.setTechnician(e.getValue()));
        technician.setWidth("10em");
        technician.setPreventInvalidInput(true);
        technician.setAllowCustomValue(false);
        return technician;
    }
}
