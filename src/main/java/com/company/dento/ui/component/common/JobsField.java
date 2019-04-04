package com.company.dento.ui.component.common;

import com.company.dento.model.business.Job;
import com.company.dento.model.business.JobTemplate;
import com.company.dento.model.business.Order;
import com.company.dento.model.business.User;
import com.company.dento.ui.component.layout.JobLayout;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JobsField extends AbstractCompositeField<VerticalLayout, JobsField, List<Job>> implements Localizable {

    private final Map<Job, JobLayout> value = new LinkedHashMap<>();
    private final ComboBox<JobTemplate> jobTemplatesField = new ComboBox<>();
    private final Button addButton = new Button();
    private final Button createButton = new Button();
    private final Accordion accordion = new Accordion();
    private Order order;

    public JobsField() {
        super(null);
        initLayout();
        this.addValueChangeListener(this::handleValueChange);
    }

    public void setJobTemplates(final List<JobTemplate> jobTemplates) {
        jobTemplatesField.setItems(jobTemplates);
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void setTechnicians(final List<User> technicians) {
        value.values().forEach(jl -> jl.setTechnicians(technicians));
    }

    @Override
    public void setPresentationValue(final List<Job> value) {
        this.value.clear();
        value.forEach(this::addJob);
        accordion.close();
    }

    public List<Job> getValue() {
        return new ArrayList<>(value.keySet());
    }

    @Override
    public void localize() {
        this.value.values().forEach(JobLayout::localize);
        addButton.setText(Localizer.getLocalizedString("add"));
        createButton.setText(Localizer.getLocalizedString("create"));
    }

    private void addJob(final Job job) {
        final AccordionPanel panel = new AccordionPanel();
        panel.setSummary(createSummary(job, panel));
        final JobLayout jobLayout = new JobLayout();
        jobLayout.setJob(job);
        jobLayout.localize();
        jobLayout.addTeethSelectListener(e -> value.values().forEach(jl -> jl.updateTeeth(this.getValue())));
        panel.setContent(jobLayout);
        accordion.add(panel);
        this.value.put(job, jobLayout);
        setModelValue(new ArrayList<>(value.keySet()), true);
    }

    private Component createSummary(final Job job, final AccordionPanel panel) {
        final HorizontalLayout summary = new HorizontalLayout();
        final Label text = new Label(job.getTemplate().getName());
        final Button removeButton = new Button();
        final Icon icon = new Icon(VaadinIcon.CLOSE_SMALL);
        removeButton.setIcon(icon);
        removeButton.addClassName("dento-grid-action");
        removeButton.addClickListener(e -> this.remove(job));
        removeButton.setWidth("3em");
        summary.add(text, removeButton);
        summary.setAlignItems(FlexComponent.Alignment.BASELINE);
        return summary;
    }

    private void remove(final Job item) {
        accordion.remove(value.get(item));
        value.remove(item);
        setModelValue(new ArrayList<>(value.keySet()), true);
    }

    private void handleValueChange(final AbstractField.ComponentValueChangeEvent<JobsField, List<Job>> event) {
        value.values().forEach(jobLayout -> jobLayout.updateTeeth(event.getValue()));
    }

    private void initLayout() {
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(jobTemplatesField, addButton, createButton);
        this.getElement().getStyle().set("overflow-y", "auto");
        this.getElement().getStyle().set("overflow-x", "hidden");
        this.getContent().add(buttonsLayout, accordion);
        this.getContent().setSizeFull();
        //addButton.addClassNames("dento-button-full");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.setWidth("6em");
        addButton.addClickListener(e -> handleAddJob());
        //createButton.addClassNames("dento-button-full");
        createButton.setWidth("6em");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonsLayout.setWidth("100%");
        buttonsLayout.setMaxWidth("650px");
        buttonsLayout.setFlexGrow(1, jobTemplatesField);
        accordion.setWidth("100%");
        jobTemplatesField.setMinWidth("7em");
        jobTemplatesField.setMaxWidth("20em");
    }

    private Job createJob(final JobTemplate jobTemplate) {
        final Job job = new Job();
        job.setOrder(order);
        job.setTemplate(jobTemplate);
        return job;
    }

    private void handleAddJob() {
        jobTemplatesField.getOptionalValue()
                .ifPresent(jobTemplate -> {
                    this.addJob(createJob(jobTemplate));
                    accordion.close();
                });
    }
}
