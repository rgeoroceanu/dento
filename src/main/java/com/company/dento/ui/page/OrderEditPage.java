package com.company.dento.ui.page;

import com.company.dento.model.business.*;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.ExecutionSelect;
import com.company.dento.ui.component.common.JobSelect;
import com.company.dento.ui.component.common.SampleSelect;
import com.company.dento.ui.component.common.TeethSelect;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "addOrder")
@Log4j2
public class OrderEditPage extends Page implements Localizable, AfterNavigationObserver {

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
    private final Upload cadField = new Upload();
    private final Checkbox paidField = new Checkbox();
    private final TextField partialSumField = new TextField();
    private final JobSelect jobSelect = new JobSelect();
    private final TeethSelect teethSelect = new TeethSelect();
    private final SampleSelect sampleSelect = new SampleSelect();
    private final ExecutionSelect executionSelect;

    private final Binder<Order> binder = new Binder<>();

    private final Label doctorLabel = new Label();
    private final Label patientLabel = new Label();
    private final Label colorLabel = new Label();
    private final Label observationsLabel = new Label();
    private final Label dateLabel = new Label();
    private final Label cadLabel = new Label();
    private final Label paidLabel = new Label();
    private final Label partialSumLabel = new Label();
    private final Label jobSelectLabel = new Label();
    private final Label teethSelectLabel = new Label();
    private final Label sampleSelectLabel = new Label();
    private final Label executionSelectLabel = new Label();

    private final FormLayout generalLayout = new FormLayout();
    private final FormLayout jobsLayout = new FormLayout();
    private final FormLayout executionsSamplesLayout = new FormLayout();


    public OrderEditPage(final DataService dataService) {
        super(dataService);
        executionSelect = new ExecutionSelect(dataService);

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        final Div footerDiv = new Div();

        initGeneralTab();
        initTeethTab();
        initExecutionsSamplesTab();
        bindGeneralFields();

        saveButton.addClassName("dento-button-full");
        discardButton.addClassName("dento-button-simple");

        buttonsLayout.addClassName("edit-layout__footer-buttons");
        footerDiv.addClassName("edit-layout__footer");

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setMargin(false);
        layout.add(tabs, generalLayout, jobsLayout, executionsSamplesLayout, footerDiv);

        generalLayout.setSizeFull();
        jobsLayout.setSizeFull();
        executionsSamplesLayout.setSizeFull();
        buttonsLayout.add(discardButton, saveButton);
        footerDiv.add(buttonsLayout);
        tabs.add(generalTab, jobsTab, executionsSamplesTab);
        tabs.addSelectedChangeListener(e -> toggleTabSelection(e.getSource().getSelectedIndex()));

        this.setContent(layout);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        jobSelect.setItems(dataService.getAll(JobTemplate.class));
        sampleSelect.setItems(dataService.getAll(SampleTemplate.class));
        executionSelect.setItems(dataService.getAll(ExecutionTemplate.class));
        doctorField.setItems(dataService.getAll(Doctor.class));
        generalTab.setSelected(true);
        toggleTabSelection(0);
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
        jobSelectLabel.setText(Localizer.getLocalizedString("jobs"));
        teethSelectLabel.setText(Localizer.getLocalizedString("teeth"));
        sampleSelectLabel.setText(Localizer.getLocalizedString("samples"));
        executionSelectLabel.setText(Localizer.getLocalizedString("executions"));
    }

    private void initGeneralTab() {
        generalLayout.addFormItem(doctorField, doctorLabel);
        generalLayout.addFormItem(dateField, dateLabel);
        generalLayout.addFormItem(patientField, patientLabel);
        generalLayout.addFormItem(colorField, colorLabel);
        generalLayout.addFormItem(partialSumField, partialSumLabel);
        generalLayout.addFormItem(paidField, paidLabel);
        generalLayout.addFormItem(observationsField, observationsLabel);
        generalLayout.addFormItem(cadField, cadLabel);

        cadField.setAutoUpload(true);
        cadField.setDropAllowed(false);

        final Button upload = new Button();
        upload.addClassName("dento-button-simple");
        upload.setIcon(new Icon(VaadinIcon.UPLOAD));
        upload.setText("");

        cadField.setUploadButton(upload);
        observationsField.setWidth("22em");
        observationsField.setHeight("8em");
        generalLayout.addClassName("dento-form-layout");
    }

    private void initTeethTab() {
        jobsLayout.addFormItem(jobSelect, jobSelectLabel).getStyle().set("align-items", "initial");
        jobsLayout.addFormItem(teethSelect, teethSelectLabel).getStyle().set("align-items", "initial");
        jobSelect.setHeight("18em");
        jobSelect.setWidth("25em");
        jobsLayout.addClassName("dento-form-layout");
    }

    private void initExecutionsSamplesTab() {
        executionsSamplesLayout.addFormItem(sampleSelect, sampleSelectLabel).getStyle().set("align-items", "initial");
        executionsSamplesLayout.addFormItem(executionSelect, executionSelectLabel).getStyle().set("align-items", "initial");
        sampleSelect.setHeight("20em");
        sampleSelect.setWidth("54em");
        executionSelect.setHeight("20em");
        executionSelect.setWidth("54em");
        executionsSamplesLayout.addClassName("dento-form-layout");
    }

    private void bindGeneralFields() {
        binder.forField(doctorField)
                .asRequired()
                .bind(Order::getDoctor, Order::setDoctor);
    }

    private void toggleTabSelection(final int index) {
        switch (index) {
            case 0:
                generalLayout.setVisible(true);
                jobsLayout.setVisible(false);
                executionsSamplesLayout.setVisible(false);
                break;
            case 1:
                generalLayout.setVisible(false);
                jobsLayout.setVisible(true);
                executionsSamplesLayout.setVisible(false);
                break;
            case 2:
                generalLayout.setVisible(false);
                jobsLayout.setVisible(false);
                executionsSamplesLayout.setVisible(true);
                break;
        }
    }
}
