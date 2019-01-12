package com.company.dento.ui.component.common;

import com.company.dento.model.business.Job;
import com.company.dento.model.business.JobTemplate;
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

public class JobSelect extends AbstractCompositeField<VerticalLayout, JobSelect, List<Job>> {

    private final ComboBox<JobTemplate> jobSelect;
    private final List<Job> value = new ArrayList<>();
    private final Grid<Job> grid;

    public JobSelect() {
        super(null);
        final HorizontalLayout buttons = new HorizontalLayout();

        grid = new Grid<>(Job.class);
        grid.getElement().setAttribute("theme", "row-stripes");
        grid.addClassName("dento-noheader-grid");
        grid.getColumns().forEach(grid::removeColumn);
        grid.addColumn("template.name").setWidth("67%").setSortable(false);
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

        grid.addClassName("dento-grid");

        final Button addButton = new Button();
        addButton.setIcon(new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("dento-button-simple",  "main-layout__content-menu-button");
        addButton.addClickListener(e -> add());

        jobSelect = new ComboBox<>();
        jobSelect.setWidth("70%");

        buttons.add(jobSelect, addButton);
        buttons.setPadding(false);
        buttons.setWidth("100%");
        this.getContent().add(buttons, grid);
        this.getContent().setPadding(false);
        this.getContent().setHeight("18em");
        this.getContent().addClassName("dento-form-field");
    }

    public void setItems(final List<JobTemplate> items) {
        value.clear();
        grid.setItems(value);
        jobSelect.setItems(items);
    }

    @Override
    public List<Job> getValue() {
        return new ArrayList<>(value);
    }

    @Override
    protected void setPresentationValue(final List<Job> jobs) {
        this.value.clear();
        this.value.addAll(jobs);
        grid.setItems(value);
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        jobSelect.setRequiredIndicatorVisible(required);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return jobSelect.isRequiredIndicatorVisible();
    }

    private void add() {
        jobSelect.getOptionalValue()
                .ifPresent(v -> {
                    final Job item = new Job();
                    item.setTemplate(v);
                    value.add(item);
                    grid.setItems(value);
                    setModelValue(value, true);
                });
    }

    private void remove(final Job item) {
        value.remove(item);
        grid.setItems(value);
        setModelValue(value, true);
    }
}
