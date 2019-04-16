package com.company.dento.ui.component.common;

import com.company.dento.model.business.*;
import com.company.dento.ui.component.layout.JobLayout;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.JobTemplateEditPage;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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

import java.util.*;

public class JobsField extends AbstractCompositeField<VerticalLayout, JobsField, Set<Job>> implements Localizable {

    private final Map<Job, JobLayout> value = new LinkedHashMap<>();
    private final ComboBox<JobTemplate> jobTemplatesField = new ComboBox<>();
    private final Button addButton = new Button();
    private final Button createButton = new Button();
    private final Accordion accordion = new Accordion();
    private Order order;
    private List<User> technicians = new ArrayList<>();

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
        this.technicians.clear();
        this.technicians.addAll(technicians);
    }

    @Override
    public void setPresentationValue(final Set<Job> value) {
        this.value.clear();
        value.forEach(this::addJob);
        accordion.close();
    }

    public Set<Job> getValue() {
        return new HashSet<>(value.keySet());
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
        jobLayout.setTechnicians(technicians);
        jobLayout.addTeethSelectListener(e -> value.values().forEach(jl -> jl.updateTeeth(this.getValue())));
        panel.setContent(jobLayout);
        accordion.add(panel);
        this.value.put(job, jobLayout);
        setModelValue(new HashSet<>(value.keySet()), true);
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
        setModelValue(new HashSet<>(value.keySet()), true);
    }

    private void handleValueChange(final AbstractField.ComponentValueChangeEvent<JobsField, Set<Job>> event) {
        value.values().forEach(jobLayout -> jobLayout.updateTeeth(event.getValue()));
    }

    private void initLayout() {
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(jobTemplatesField, addButton, createButton);
        this.getElement().getStyle().set("overflow-y", "auto");
        this.getElement().getStyle().set("overflow-x", "hidden");
        this.getContent().add(buttonsLayout, accordion);
        this.getContent().setSizeFull();

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.setWidth("6em");
        addButton.addClickListener(e -> handleAddJob());

        createButton.setWidth("6em");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickListener(e -> UI.getCurrent().navigate(JobTemplateEditPage.class));

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

        jobTemplate.getSampleTemplates().stream()
                .map(template -> {
                    final Sample sample = new Sample();
                    sample.setJob(job);
                    sample.setTemplate(template);
                    return sample;
                }).forEach(sample -> job.getSamples().add(sample));

        jobTemplate.getExecutionTemplates().stream()
                .map(template -> {
                    final Execution execution = new Execution();
                    execution.setTemplate(template);
                    execution.setJob(job);
                    return execution;
                }).forEach(execution -> job.getExecutions().add(execution));

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
