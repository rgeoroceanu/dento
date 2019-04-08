package com.company.dento.ui.page.edit;

import com.company.dento.model.business.*;
import com.company.dento.service.DataService;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@UIScope
//@Component
@Secured(value = {"USER", "ADMIN"})
@Route(value = "order")
@Log4j2
public class OrderEditPage extends EditPage<Order> {

    private final Tabs tabs = new Tabs();
    private final Tab generalTab = new Tab();
    private final Tab jobsTab = new Tab();
    private final Tab executionsSamplesTab = new Tab();
    private final ComboBox<Doctor> doctorField = new ComboBox<>();
    private final TextField patientField = new TextField();
    private final ComboBox<Color> colorField = new ComboBox<>();
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
        super(dataService);

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
        generalTab.setSelected(true);
        toggleTabSelection(0);
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
        colorField.setItems(dataService.getAll(Color.class));
        jobsField.setTechnicians(dataService.getAll(User.class));
        jobsField.setJobTemplates(dataService.getAll(JobTemplate.class));
    }

    private void initGeneralTab() {
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
        colorField.setItemLabelGenerator(Color::getName);
    }

    protected void bindFields() {
        binder.forField(doctorField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Order::getDoctor, Order::setDoctor);

        binder.forField(patientField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .withValidator(new StringLengthValidator(String.format(Localizer
                        .getLocalizedString("stringLengthValidation"), 5, 255), 5, 255))
                .bind(Order::getPatient, Order::setPatient);

        binder.forField(dateField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Order::getDate, Order::setDate);

        binder.forField(colorField)
                .bind(Order::getColor, Order::setColor);

        binder.forField(partialSumField)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("integerRangeValidation", 0, 100000))
                .bind(Order::getPartialSum, Order::setPartialSum);

        binder.forField(paidField)
                .bind(Order::isPaid, Order::setPaid);

        binder.forField(observationsField)
                .withValidator(new StringLengthValidator(String.format(Localizer
                        .getLocalizedString("stringLengthValidation"), 0, 4000), 0, 4000))
                .bind(Order::getDescription, Order::setDescription);

        binder.forField(uploadField)
                //.asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Order::getCadFiles, Order::setCadFiles);

        binder.forField(jobsField)
                .bind(Order::getJobs, Order::setJobs);

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

    protected void save() {
        if (binder.isValid()) {
            final Order item = binder.getBean();
            item.getJobs().forEach(job -> job.setOrder(item));
            dataService.saveEntity(item);
            UI.getCurrent().navigate(OrdersPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(OrdersPage.class);
    }
}
