package com.company.dento.ui.page.edit;

import com.company.dento.model.business.*;
import com.company.dento.model.type.SelectionType;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.DefaultMaterialField;
import com.company.dento.ui.component.common.DentoNotification;
import com.company.dento.ui.component.common.PriceField;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.JobTemplatesPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.validator.FloatRangeValidator;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Optional;
import java.util.stream.Collectors;

@UIScope
@Component
@Route(value = "admin/jobs/id")
@Log4j2
public class JobTemplateEditPage extends EditPage<JobTemplate> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final ComboBox<SelectionType> selectionTypeField = new ComboBox<>();
    private final Checkbox activeField = new Checkbox();
    private final TextField standardPriceField = new TextField();
    private final MultiselectComboBox<SampleTemplate> sampleTemplatesField = new MultiselectComboBox<>();
    private final MultiselectComboBox<ExecutionTemplate> executionTemplatesField = new MultiselectComboBox<>();
    private final DefaultMaterialField materialsField = new DefaultMaterialField();
    private final PriceField<JobPrice, Clinic> individualPricesField = new PriceField<>(JobPrice.class);

    private final Label nameLabel = new Label();
    private final Label selectionTypeLabel = new Label();
    private final Label standardPriceLabel = new Label();
    private final Label activeLabel = new Label();
    private final Label sampleTemplatesLabel = new Label();
    private final Label executionTemplatesLabel = new Label();
    private final Label materialsLabel = new Label();
    private final Label individualPricesLabel = new Label();

    public JobTemplateEditPage(final DataService dataService) {
        super(dataService, "Lucrare");

        initGeneralLayout();
        reload();
        bindFields();

        contentLayout.addComponentAtIndex(0, generalLayout);
        generalLayout.setSizeFull();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        super.afterNavigation(afterNavigationEvent);
    }

    @Override
    public void setParameter(final BeforeEvent beforeEvent, final @OptionalParameter Long itemId) {
        reload();

        final Optional<JobTemplate> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, JobTemplate.class);
        } else {
            item = Optional.of(new JobTemplate());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(JobTemplatesPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        selectionTypeLabel.setText(Localizer.getLocalizedString("selectionType"));
        standardPriceLabel.setText(Localizer.getLocalizedString("standardPrice"));
        activeLabel.setText(Localizer.getLocalizedString("active"));
        sampleTemplatesLabel.setText(Localizer.getLocalizedString("samples"));
        executionTemplatesLabel.setText(Localizer.getLocalizedString("executions"));
        materialsLabel.setText(Localizer.getLocalizedString("materials"));
        individualPricesLabel.setText(Localizer.getLocalizedString("individualPrices"));
        materialsField.localize();
    }

    protected void reload() {
        selectionTypeField.setItems(SelectionType.values());
        sampleTemplatesField.setItems(dataService.getAll(SampleTemplate.class)
                .stream().filter(SampleTemplate::isActive));
        executionTemplatesField.setItems(dataService.getAll(ExecutionTemplate.class)
                .stream().filter(ExecutionTemplate::isActive));
        materialsField.setOptions(dataService.getAll(MaterialTemplate.class)
                .stream().filter(MaterialTemplate::isActive).collect(Collectors.toList()));
        individualPricesField.setOptions(dataService.getAll(Clinic.class));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(selectionTypeField, selectionTypeLabel);
        generalLayout.addFormItem(sampleTemplatesField, sampleTemplatesLabel).getStyle().set("align-items", "end").set("margin-bottom", "25px");
        generalLayout.addFormItem(executionTemplatesField, executionTemplatesLabel).getStyle().set("align-items", "end").set("margin-bottom", "25px");;
        generalLayout.addFormItem(standardPriceField, standardPriceLabel);
        generalLayout.addFormItem(individualPricesField, individualPricesLabel).getStyle().set("align-items", "end");
        generalLayout.addFormItem(materialsField, materialsLabel).getStyle().set("align-items", "end");
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        selectionTypeField.addClassName("dento-form-field");
        standardPriceField.addClassName("dento-form-field");
        activeField.addClassName("dento-form-field");
        sampleTemplatesField.addClassName("dento-form-field");
        executionTemplatesField.addClassName("dento-form-field");
        selectionTypeField.setItemLabelGenerator(val -> Localizer.getLocalizedString(val.name().toLowerCase()));
        executionTemplatesField.setItemLabelGenerator(ExecutionTemplate::getName);
        sampleTemplatesField.setItemLabelGenerator(SampleTemplate::getName);
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired("Introduceți nume!")
                .bind(JobTemplate::getName, JobTemplate::setName);

        binder.forField(selectionTypeField)
                .asRequired("Alegeți tip selecție!")
                .bind(JobTemplate::getSelectionType, JobTemplate::setSelectionType);

        binder.forField(standardPriceField)
                .withConverter(new StringToFloatConverter("Introduceți preț!"))
                .withValidator(new FloatRangeValidator("Introduceți valoare preț între 0 și 100000!", 0f, 100000f))
                .bind(JobTemplate::getStandardPrice, JobTemplate::setStandardPrice);

        binder.forField(activeField)
                .bind(JobTemplate::isActive, JobTemplate::setActive);

        binder.forField(sampleTemplatesField)
                .bind(JobTemplate::getSampleTemplates, JobTemplate::setSampleTemplates);

        binder.forField(executionTemplatesField)
                .bind(JobTemplate::getExecutionTemplates, JobTemplate::setExecutionTemplates);

        binder.forField(materialsField)
                .bind(JobTemplate::getMaterials, JobTemplate::setMaterials);

        binder.forField(individualPricesField)
                .bind(JobTemplate::getIndividualPrices, JobTemplate::setIndividualPrices);

    }

    protected void save() {
        final BinderValidationStatus<JobTemplate> status = binder.validate();

        if (!status.hasErrors()) {
            final JobTemplate item = binder.getBean();
            dataService.saveEntity(item);
            DentoNotification.success("Datele s-au salvat cu success!");
            UI.getCurrent().navigate(JobTemplatesPage.class);
        } else {
            DentoNotification.error("Date invalide!",
                    status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.toList()));
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(JobTemplatesPage.class);
    }
}
