package com.company.dento.ui.component.common;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.User;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExecutionSelect extends AbstractCompositeField<VerticalLayout, ExecutionSelect, Set<Execution>> implements Localizable {

    private final List<Execution> value = new ArrayList<>();
    private final Grid<Execution> grid;
    private final List<User> technicians = new ArrayList<>();
    private final Label emptyText = new Label();
    private final FooterRow footer;

    public ExecutionSelect() {
        super(null);

        grid = new Grid<>(Execution.class);
        grid.getElement().setAttribute("theme", "row-stripes no-border");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("template.name").setSortable(false);
        grid.addComponentColumn(this::addTechnicianColumn).setKey("technician").setFlexGrow(0).setWidth("20em").setSortable(false);
        grid.addClassName("dento-grid");
        grid.setHeightByRows(true);

        footer = grid.appendFooterRow();
        footer.getCells().get(0).setComponent(emptyText);

        this.getContent().add(grid);
        this.getContent().setPadding(false);
        this.getContent().getStyle().set("margin-bottom", "15px");
        this.getContent().setMaxHeight("20em");
        this.getContent().setWidth("90%");
        this.getContent().getStyle().set("min-width", "200px");
        this.getContent().getStyle().set("max-width", "650px");
    }

    @Override
    public void localize() {
        grid.getColumns().get(0).setHeader(Localizer.getLocalizedString("name"));
        grid.getColumnByKey("technician").setHeader(Localizer.getLocalizedString("technician"));
        emptyText.setText(Localizer.getLocalizedString("emptyExecutions"));
    }

    public void setTechnicians(final List<User> technicians) {
        this.technicians.clear();
        this.technicians.addAll(technicians);
    }

    @Override
    public void setPresentationValue(final Set<Execution> value) {
        this.value.clear();
        this.value.addAll(value);
        grid.setItems(value);
        emptyText.setVisible(value.isEmpty());
    }


    @Override
    public Set<Execution> getValue() {
        return new HashSet<>(value);
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
