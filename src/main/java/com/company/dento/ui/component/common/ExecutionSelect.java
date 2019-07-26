package com.company.dento.ui.component.common;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionPrice;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.User;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class ExecutionSelect extends ListSelectField<Execution, ExecutionTemplate> {

    private List<User> technicians = new ArrayList<>();

    public ExecutionSelect() {
        super(Execution.class);
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("technician").setHeader(Localizer.getLocalizedString("technician"));
    }

    public void setTechnicians(final List<User> technicians) {
        this.technicians.clear();
        this.technicians.addAll(technicians);
    }

    @Override
    protected ItemLabelGenerator<ExecutionTemplate> getOptionsLabelGenerator() {
        return ExecutionTemplate::getName;
    }

    @Override
    protected void addColumns() {
        grid.addColumn("template.name").setSortable(false);
        grid.addComponentColumn(this::addTechnicianColumn).setKey("technician").setFlexGrow(0).setWidth("20em").setSortable(false);
    }

    @Override
    protected Execution createNewItem(final ExecutionTemplate option) {
        final Execution item = new Execution();
        item.setTemplate(option);
        item.setPrice(option.getStandardPrice());
        return item;
    }

    @Override
    protected Long getItemOptionId(final Execution item) {
        return item.getTemplate().getId();
    }

    private ComboBox<User> addTechnicianColumn(final Execution execution) {
        final ComboBox<User> technician = new ComboBox<>();
        technician.setItems(technicians);
        technician.setValue(execution.getTechnician());
        technician.addValueChangeListener(e -> updateTechnician(execution, e.getValue()));
        technician.setWidth("15em");
        technician.setPreventInvalidInput(true);
        technician.setAllowCustomValue(false);
        technician.addClassName("dento-grid-filter-small");
        technician.addValueChangeListener(v -> setModelValue(value, true));
        return technician;
    }

    private void updateTechnician(final Execution execution, final User technician) {
        execution.setTechnician(technician);
    }
}
