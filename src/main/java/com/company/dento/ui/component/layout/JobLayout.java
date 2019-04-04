package com.company.dento.ui.component.layout;

import com.company.dento.model.business.Job;
import com.company.dento.model.business.Tooth;
import com.company.dento.model.business.User;
import com.company.dento.ui.component.common.ExecutionSelect;
import com.company.dento.ui.component.common.SampleSelect;
import com.company.dento.ui.component.common.TeethSelect;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class JobLayout extends FormLayout implements Localizable {

    private final DatePicker dateField = new DatePicker();
    private final TimePicker timeField = new TimePicker();
    private final TeethSelect teethSelect = new TeethSelect();
    private final SampleSelect sampleSelect = new SampleSelect();
    private final ExecutionSelect executionSelect = new ExecutionSelect();

    private final Label dateLabel = new Label();
    private final Label teethSelectLabel = new Label();
    private final Label sampleSelectLabel = new Label();
    private final Label executionSelectLabel = new Label();

    private Binder<Job> binder = new Binder<>();

    public JobLayout() {
        initLayout();
        bindFields();
    }

    public void setTechnicians(final List<User> technicians) {
        executionSelect.setTechnicians(technicians);
    }

    @Override
    public void localize() {
        dateLabel.setText(Localizer.getLocalizedString("deliveryDate"));
        teethSelectLabel.setText(Localizer.getLocalizedString("teeth"));
        sampleSelectLabel.setText(Localizer.getLocalizedString("samples"));
        executionSelectLabel.setText(Localizer.getLocalizedString("executions"));
        sampleSelect.localize();
        executionSelect.localize();
    }

    public void setJob(final Job job) {
        binder.setBean(job);
    }

    public void updateTeeth(final List<Job> allJobs) {
        final Set<Integer> disabled = extractTeeth(allJobs);
        disabled.removeAll(binder.getBean().getTeeth().stream().map(Tooth::getNumber).collect(Collectors.toList()));
        teethSelect.setDisabledTeeth(disabled);
    }

    public void addTeethSelectListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TeethSelect, Set<Tooth>>> listener) {
        teethSelect.addValueChangeListener(listener);
    }

    public Job getJob() {
        return binder.getBean();
    }

    private Set<Integer> extractTeeth(final List<Job> allJobs) {
        return allJobs.stream()
                .map(Job::getTeeth)
                .flatMap(Set::stream)
                .map(Tooth::getNumber)
                .collect(Collectors.toSet());
    }

    private void initLayout() {
        final FormLayout fl1 = new FormLayout();
        final FormLayout fl2 = new FormLayout();
        final HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.add(dateField, timeField);
        dateLayout.setWidth("90%");
        dateField.setWidth("49%");
        timeField.setWidth("49%");
        dateField.setMaxWidth("11em");
        timeField.setMaxWidth("11em");
        dateLayout.getStyle().set("max-width", "650px");
        dateField.setLocale(Locale.GERMAN);
        timeField.setLocale(Locale.GERMAN);
        final FormLayout.FormItem fi1 = fl1.addFormItem(dateLayout, dateLabel);
        final FormLayout.FormItem fi2 = fl1.addFormItem(sampleSelect, sampleSelectLabel);
        final FormLayout.FormItem fi3 = fl1.addFormItem(executionSelect, executionSelectLabel);
        final FormLayout.FormItem fi4 = fl2.addFormItem(teethSelect, teethSelectLabel);
        fi1.getStyle().set("align-items", "initial");
        fi2.getStyle().set("align-items", "initial");
        fi3.getStyle().set("align-items", "initial");
        fi4.getStyle().set("align-items", "initial");
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        final FormLayout.ResponsiveStep rs3 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs4 = new FormLayout.ResponsiveStep("1300px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        this.addFormItem(fl1, new Label());
        this.addFormItem(fl2, new Label());
        fl1.setResponsiveSteps(rs1, rs2);
        fl2.setResponsiveSteps(rs1, rs2);
        this.setResponsiveSteps(rs3, rs4);
        this.addClassName("dento-form-layout");
        fl1.addClassName("dento-form-layout");
        fl2.addClassName("dento-form-layout");
        this.setSizeFull();
        this.getStyle().set("margin", "0px");
    }

    private void bindFields() {
        binder.forField(dateField)
                .bind(Job::getDeliveryDate, Job::setDeliveryDate);

        binder.forField(timeField)
                .bind(Job::getDeliveryTime, Job::setDeliveryTime);

        binder.forField(teethSelect)
                //.asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Job::getTeeth, Job::setTeeth);

        binder.forField(executionSelect)
                //.asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Job::getExecutions, Job::setExecutions);

        binder.forField(sampleSelect)
                //.asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Job::getSamples, Job::setSamples);
    }
}