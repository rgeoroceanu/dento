package com.company.dento.ui.page.edit;

import com.company.dento.model.business.*;
import com.company.dento.model.type.ToothOptionColumn;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.DentoNotification;
import com.company.dento.ui.component.common.JobsField;
import com.company.dento.ui.component.common.UploadField;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.OrdersPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UIScope
@Component
@Route(value = "orders/id")
@Log4j2
public class OrderEditPage extends EditPage<Order> {

    private final Tabs tabs = new Tabs();
    private final Tab generalTab = new Tab();
    private final Tab jobsTab = new Tab();
    private final Tab executionsSamplesTab = new Tab();
    private final ComboBox<Doctor> doctorField = new ComboBox<>();
    private final TextField patientField = new TextField();
    private final ComboBox<ToothColor> colorField = new ComboBox<>();
    private final TextArea observationsField = new TextArea();
    private final DatePicker dateField = new DatePicker();
    private final UploadField uploadField = new UploadField();
    private final Checkbox paidField = new Checkbox();
    private final TextField partialSumField = new TextField();
    private final JobsField jobsField = new JobsField();

    private final Label doctorLabel = new Label();
    private final Label patientLabel = new Label();
    private final Label colorLabel = new Label();
    private final Label observationsLabel = new Label();
    private final Label dateLabel = new Label();
    private final Label cadLabel = new Label();
    private final Label paidLabel = new Label();
    private final Label partialSumLabel = new Label();

    private final FormLayout generalLayout = new FormLayout();

    public OrderEditPage(final DataService dataService) {
        super(dataService, "Comandă");

        initGeneralTab();
        reload();
        bindFields();

        generalLayout.setSizeFull();
        tabs.add(generalTab, jobsTab);
        tabs.addSelectedChangeListener(e -> toggleTabSelection(e.getSource().getSelectedIndex()));

        contentLayout.addComponentAtIndex(0, tabs);
        contentLayout.addComponentAtIndex(1, generalLayout);
    }

    @Override
    public void afterNavigation(final AfterNavigationEvent afterNavigationEvent) {
        super.afterNavigation(afterNavigationEvent);
        toggleTabSelection(0);
        tabs.setSelectedIndex(0);
        generalTab.setSelected(true);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long orderId) {
        reload();

        final Optional<Order> order;
        if (orderId != null) {
            order = dataService.getEntity(orderId, Order.class);
        } else {
            order = Optional.of(new Order());
        }

        if (order.isPresent()) {
            binder.setBean(order.get());
            jobsField.setOrder(order.get());
        } else {
            beforeEvent.rerouteTo(OrdersPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        generalTab.setLabel(Localizer.getLocalizedString("generalInformation"));
        jobsTab.setLabel(Localizer.getLocalizedString("jobs"));
        executionsSamplesTab.setLabel(Localizer.getLocalizedString("executionsSamples"));
        doctorLabel.setText(Localizer.getLocalizedString("doctor"));
        patientLabel.setText(Localizer.getLocalizedString("patient"));
        colorLabel.setText(Localizer.getLocalizedString("color"));
        observationsLabel.setText(Localizer.getLocalizedString("observations"));
        dateLabel.setText(Localizer.getLocalizedString("date"));
        cadLabel.setText(Localizer.getLocalizedString("cadData"));
        paidLabel.setText(Localizer.getLocalizedString("paidStatus"));
        partialSumLabel.setText(Localizer.getLocalizedString("partialSum"));
        jobsField.localize();
    }

    protected void reload() {
        doctorField.setItems(dataService.getAll(Doctor.class));
        colorField.setItems(dataService.getAll(ToothColor.class));
        jobsField.setTechnicians(dataService.getAll(User.class).stream().filter(User::isActive).collect(Collectors.toList()));

        jobsField.setColumn1Options(dataService.getAll(ToothOption.class).stream()
                .filter(o -> o.getDisplayColumn().equals(ToothOptionColumn.COLOANA_1))
                .collect(Collectors.toList()));

        jobsField.setColumn2Options(dataService.getAll(ToothOption.class).stream()
                .filter(o -> o.getDisplayColumn().equals(ToothOptionColumn.COLOANA_2))
                .collect(Collectors.toList()));

        jobsField.setJobTemplates(dataService.getAll(JobTemplate.class)
                .stream().filter(JobTemplate::isActive).collect(Collectors.toList()));

        jobsField.setMaterialTemplates(dataService.getAll(MaterialTemplate.class)
                .stream().filter(MaterialTemplate::isActive).collect(Collectors.toList()));

        jobsField.setExecutionTemplates(dataService.getAll(ExecutionTemplate.class)
                .stream().filter(ExecutionTemplate::isActive).collect(Collectors.toList()));

        jobsField.setSampleTemplates(dataService.getAll(SampleTemplate.class)
                .stream().filter(SampleTemplate::isActive).collect(Collectors.toList()));
    }

    private void initGeneralTab() {
        doctorField.setItemLabelGenerator(doctor -> String.format("%s %s (%s)", doctor.getFirstName(),
                doctor.getLastName(), doctor.getClinic().getName()));

        doctorField.addValueChangeListener(e -> {
            if (e.isFromClient()) updateJobPrices(binder.getBean(), e.getValue());
        });
        generalLayout.addFormItem(doctorField, doctorLabel);
        generalLayout.addFormItem(dateField, dateLabel);
        generalLayout.addFormItem(patientField, patientLabel);
        generalLayout.addFormItem(colorField, colorLabel);
        generalLayout.addFormItem(partialSumField, partialSumLabel);
        generalLayout.addFormItem(paidField, paidLabel);
        generalLayout.addFormItem(observationsField, observationsLabel);
        generalLayout.addFormItem(uploadField, cadLabel);
        generalLayout.addClassName("dento-form-layout");
        doctorField.addClassName("dento-form-field");
        dateField.addClassName("dento-form-field");
        patientField.addClassName("dento-form-field");
        colorField.addClassName("dento-form-field");
        partialSumField.addClassName("dento-form-field");
        paidField.addClassName("dento-form-field");
        observationsField.addClassName("dento-form-field");
        observationsField.setHeight("6em");
        colorField.setItemLabelGenerator(ToothColor::getName);
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(doctorField)
                .asRequired("Alegeți doctorul!")
                .bind(Order::getDoctor, Order::setDoctor);

        binder.forField(patientField)
                .asRequired("Introduceți numele pacientului!")
                .withValidator(new StringLengthValidator("Pacient: minim 5, maxim 255 caractere", 5, 255))
                .bind(Order::getPatient, Order::setPatient);

        binder.forField(dateField)
                .asRequired("Introduceți data comenzii!")
                .bind(Order::getDate, Order::setDate);

        binder.forField(colorField)
                .bind(Order::getToothColor, Order::setToothColor);

        binder.forField(partialSumField)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("Suma parțială: valoare între 0 și 100000", 0, 100000))
                .bind(Order::getPartialSum, Order::setPartialSum);

        binder.forField(paidField)
                .bind(Order::isPaid, Order::setPaid);

        binder.forField(observationsField)
                .withValidator(new StringLengthValidator("Observații: maxim 4000 de caractere!", 0, 4000))
                .bind(Order::getDescription, Order::setDescription);

        binder.forField(uploadField)
                //.asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Order::getStoredFiles, Order::setStoredFiles);

        binder.forField(jobsField)
                .withValidator((jobs,c) -> areJobExecutionsValid(jobs))
                .withValidator((jobs,c) -> areJobDatesValid(jobs))
                .bind(Order::getJobs, Order::setJobs);

    }

    private ValidationResult areJobExecutionsValid(final Set<Job> jobs) {
        return jobs.stream()
                .map(Job::getExecutions)
                .flatMap(Collection::stream)
                .filter(e -> e.getTechnician() == null)
                .findAny()
                .map(e -> ValidationResult.error("Vă rugăm să alegeți tehnicianul pentru manopeă!"))
                .orElse(ValidationResult.ok());
    }

    private ValidationResult areJobDatesValid(final Set<Job> jobs) {
        return jobs.stream()
                .filter(j -> j.getDeliveryDate() == null)
                .findAny()
                .map(e -> ValidationResult.error("Vă rugăm să alegeți data de predare a lucrării!"))
                .orElse(ValidationResult.ok());
    }

    private void toggleTabSelection(final int index) {
        switch (index) {
            case 0:
                contentLayout.replace(contentLayout.getComponentAt(1), generalLayout);
                break;
            case 1:
                contentLayout.replace(contentLayout.getComponentAt(1), jobsField);
                break;
        }
    }

    private void updateJobPrices(final Order order, final Doctor doctor) {
        order.getJobs().forEach(job -> {
            final float price = job.getTemplate().getIndividualPrices().stream()
                    .filter(p -> doctor != null && doctor.getClinic() != null && p.getClinic() != null)
                    .filter(p -> p.getClinic().getId().equals(doctor.getClinic().getId()))
                    .map(JobPrice::getPrice)
                    .findFirst()
                    .orElse(job.getTemplate().getStandardPrice());

            job.setPrice(price);
        });
        jobsField.setValue(order.getJobs());
    }

    protected void save() {
        final BinderValidationStatus<Order> status = binder.validate();

        if (!status.hasErrors()) {
            final Order item = binder.getBean();
            item.getJobs().forEach(job -> job.setOrder(item));
            dataService.saveOrder(item);
            DentoNotification.success("Comanda salvată!");
            UI.getCurrent().navigate(OrdersPage.class);
        } else {
            DentoNotification.error("Date invalide!",
                    status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.toList()));
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(OrdersPage.class);
    }
}
