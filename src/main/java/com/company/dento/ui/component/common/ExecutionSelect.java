package com.company.dento.ui.component.common;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class ExecutionSelect extends AbstractCompositeField<VerticalLayout, ExecutionSelect, List<Execution>> implements Localizable {

    private final List<Execution> value = new ArrayList<>();
    private final Grid<Execution> grid;
    private final List<User> technicians = new ArrayList<>();

    public ExecutionSelect() {
        super(null);

        grid = new Grid<>(Execution.class);
        grid.getElement().setAttribute("theme", "row-stripes");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("template.name").setWidth("300px").setSortable(false);
        grid.addColumn("job.template.name").setSortable(false);
        grid.addComponentColumn(this::addTechnicianColumn).setKey("technician").setSortable(false);
        grid.addClassName("dento-grid");

        this.getContent().add(grid);
        this.getContent().setPadding(false);
        this.getContent().getStyle().set("margin-bottom", "15px");
        this.getContent().setHeight("20em");
        this.getContent().setWidth("90%");
        this.getContent().getStyle().set("min-width", "200px");
    }

    @Override
    public void localize() {
        grid.getColumns().get(1).setHeader(Localizer.getLocalizedString("job"));
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("technician").setHeader(Localizer.getLocalizedString("technician"));
    }

    public void setTechnicians(final List<User> technicians) {
        this.technicians.clear();
        this.technicians.addAll(technicians);
    }

    @Override
    public void setPresentationValue(final List<Execution> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
    }

    @Override
    public List<Execution> getValue() {
        return new ArrayList<>(value);
    }

    private ComboBox<User> addTechnicianColumn(final Execution execution) {
        final ComboBox<User> technician = new ComboBox<>();
        technician.setItems(technicians);
        technician.setValue(execution.getTechnician());
        technician.addValueChangeListener(e -> execution.setTechnician(e.getValue()));
        technician.setWidth("15em");
        technician.setPreventInvalidInput(true);
        technician.setAllowCustomValue(false);
        technician.addClassName("dento-grid-filter-small");
        return technician;
    }
}
