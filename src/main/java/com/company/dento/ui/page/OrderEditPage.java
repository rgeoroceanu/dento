package com.company.dento.ui.page;

import com.company.dento.model.business.*;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.JobsField;
import com.company.dento.ui.component.common.UploadField;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

import java.util.Optional;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "order")
@Log4j2
public class OrderEditPage extends Page implements Localizable, AfterNavigationObserver, HasUrlParameter<Long> {

    private final Tabs tabs = new Tabs();
    private final Tab generalTab = new Tab();
    private final Tab jobsTab = new Tab();
    private final Tab executionsSamplesTab = new Tab();
    private final Button saveButton = new Button();
    private final Button discardButton = new Button();
    private final ComboBox<Doctor> doctorField = new ComboBox<>();
    private final TextField patientField = new TextField();
    private final ComboBox<Color> colorField = new ComboBox<>();
    private final TextArea observationsField = new TextArea();
    private final DatePicker dateField = new DatePicker();
    private final UploadField uploadField = new UploadField();
    private final Checkbox paidField = new Checkbox();
    private final TextField partialSumField = new TextField();
    private final JobsField jobsField = new JobsField();

    private final Binder<Order> binder = new Binder<>();

    private final Label doctorLabel = new Label();
    private final Label patientLabel = new Label();
    private final Label colorLabel = new Label();
    private final Label observationsLabel = new Label();
    private final Label dateLabel = new Label();
    private final Label cadLabel = new Label();
    private final Label paidLabel = new Label();
    private final Label partialSumLabel = new Label();

    private final FormLayout generalLayout = new FormLayout();
    private final VerticalLayout contentLayout = new VerticalLayout();

    public OrderEditPage(final DataService dataService) {
        super(dataService);

        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        final Div footerDiv = new Div();

        initGeneralTab();

        reload();
        bindFields();

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        discardButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveButton.addClickListener(e -> this.save());
        discardButton.addClickListener(e -> this.discard());

        buttonsLayout.addClassName("edit-layout__footer-buttons");
        footerDiv.addClassName("edit-layout__footer");

        contentLayout.setSizeFull();
        contentLayout.setPadding(false);
        contentLayout.setMargin(false);
        contentLayout.add(tabs, generalLayout, footerDiv);

        generalLayout.setSizeFull();
        buttonsLayout.add(discardButton, saveButton);
        footerDiv.add(buttonsLayout);
        tabs.add(generalTab, jobsTab);
        tabs.addSelectedChangeListener(e -> toggleTabSelection(e.getSource().getSelectedIndex()));

        this.setContent(contentLayout);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
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
        saveButton.setText(Localizer.getLocalizedString("save"));
        discardButton.setText(Localizer.getLocalizedString("cancel"));
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

    private void reload() {
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

    private void bindFields() {
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

    private void save() {
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

    private void discard() {
        UI.getCurrent().navigate(OrdersPage.class);
    }
}
