package com.company.dento.ui.page.edit;

import com.company.dento.model.business.*;
import com.company.dento.model.type.SelectionType;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.MaterialField;
import com.company.dento.ui.component.common.PriceField;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.JobTemplatesPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
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
    private final MaterialField materialsField = new MaterialField();
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
        sampleTemplatesField.setItems(dataService.getAll(SampleTemplate.class));
        executionTemplatesField.setItems(dataService.getAll(ExecutionTemplate.class));
        materialsField.setTemplates(dataService.getAll(MaterialTemplate.class));
        individualPricesField.setOptions(dataService.getAll(Clinic.class));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(selectionTypeField, selectionTypeLabel);
        generalLayout.addFormItem(sampleTemplatesField, sampleTemplatesLabel).getStyle().set("align-items", "end");
        generalLayout.addFormItem(executionTemplatesField, executionTemplatesLabel).getStyle().set("align-items", "end");
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
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(JobTemplate::getName, JobTemplate::setName);

        binder.forField(selectionTypeField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(JobTemplate::getSelectionType, JobTemplate::setSelectionType);

        binder.forField(standardPriceField)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("integerRangeValidation", 0, 100000))
                .bind(JobTemplate::getStandardPrice, JobTemplate::setStandardPrice);

        binder.forField(activeField)
                .bind(JobTemplate::isActive, JobTemplate::setActive);

        binder.forField(sampleTemplatesField)
                .bind(JobTemplate::getSampleTemplates, JobTemplate::setSampleTemplates);

        binder.forField(executionTemplatesField)
                .bind(JobTemplate::getExecutionTemplates, JobTemplate::setExecutionTemplates);

        binder.forField(materialsField)
                .bind(jt -> jt.getMaterials().stream()
                        .map(mat -> new Material(mat.getTemplate(), null, 0f, mat.getQuantity()))
                                .collect(Collectors.toSet()),
                        (jt, materials) -> jt.setMaterials(materials.stream()
                                .map(m -> new DefaultMaterial(m.getTemplate(), m.getQuantity()))
                                .collect(Collectors.toSet())));

        binder.forField(individualPricesField)
                .bind(JobTemplate::getIndividualPrices, JobTemplate::setIndividualPrices);

    }

    protected void save() {
        if (binder.isValid()) {
            final JobTemplate item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(JobTemplatesPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(JobTemplatesPage.class);
    }
}
