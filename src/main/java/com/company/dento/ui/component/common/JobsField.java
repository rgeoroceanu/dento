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
import com.vaadin.flow.component.notification.Notification;
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
    private final List<User> technicians = new ArrayList<>();
    private final List<ToothOption> optionsColumn1 = new ArrayList<>();
    private final List<ToothOption> optionsColumn2 = new ArrayList<>();
    private final List<MaterialTemplate> materialTemplates = new ArrayList<>();
    private final List<ExecutionTemplate> executionTemplates = new ArrayList<>();
    private final List<SampleTemplate> sampleTemplates = new ArrayList<>();

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

    public void setExecutionTemplates(final List<ExecutionTemplate> executionTemplates) {
        this.executionTemplates.clear();
        this.executionTemplates.addAll(executionTemplates);
    }

    public void setSampleTemplates(final List<SampleTemplate> sampleTemplates) {
        this.sampleTemplates.clear();
        this.sampleTemplates.addAll(sampleTemplates);
    }

    public void setColumn1Options(final List<ToothOption> optionsColumn1) {
        this.optionsColumn1.clear();
        this.optionsColumn1.addAll(optionsColumn1);
    }

    public void setColumn2Options(final List<ToothOption> optionsColumn2) {
        this.optionsColumn2.clear();
        this.optionsColumn2.addAll(optionsColumn2);
    }

    public void setMaterialTemplates(final List<MaterialTemplate> materialTemplates) {
        this.materialTemplates.clear();
        this.materialTemplates.addAll(materialTemplates);
    }

    @Override
    public void setPresentationValue(final Set<Job> value) {
        accordion.close();
        accordion.getChildren().forEach(accordion::remove);
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
        jobLayout.setTeethOptions(this.optionsColumn1, this.optionsColumn2);
        jobLayout.addTeethSelectListener(e -> value.values().forEach(jl -> jl.updateTeeth(this.getValue())));
        jobLayout.setMaterialTemplates(materialTemplates);
        jobLayout.setExecutionTemplates(executionTemplates);
        jobLayout.setSampleTemplates(sampleTemplates);
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
        job.setPrice(extractDefaultPrice(jobTemplate, order));

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

        jobTemplate.getMaterials().stream()
                .map(defaultMaterial -> {
                    final Material material = new Material();
                    material.setTemplate(defaultMaterial.getTemplate());
                    material.setJob(job);
                    material.setPrice(defaultMaterial.getTemplate().getPricePerUnit());
                    material.setQuantity(defaultMaterial.getQuantity());
                    return material;
                }).forEach(material -> job.getMaterials().add(material));

        return job;
    }

    private void handleAddJob() {
        if (order.getDoctor() == null) {
            Notification.show("Selectați clinica mai intâi!", 5000, Notification.Position.BOTTOM_CENTER);
            return;
        }

        jobTemplatesField.getOptionalValue()
                .ifPresent(jobTemplate -> {
                    this.addJob(createJob(jobTemplate));
                    accordion.close();
                });
    }

    private float extractDefaultPrice(final JobTemplate jobTemplate, final Order order) {
        return jobTemplate.getIndividualPrices().stream()
                .filter(p -> p.getClinic().getId().equals(order.getDoctor().getClinic() != null ? order.getDoctor().getClinic().getId() : null))
                .map(JobPrice::getPrice)
                .findFirst()
                .orElse(jobTemplate.getStandardPrice());
    }
}
