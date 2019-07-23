package com.company.dento.ui.page.edit;

import com.company.dento.model.business.JobTemplate;
import com.company.dento.model.business.MaterialPrice;
import com.company.dento.model.business.MaterialTemplate;
import com.company.dento.model.type.MeasurementUnit;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.DentoNotification;
import com.company.dento.ui.component.common.PriceField;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.MaterialTemplatesPage;
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

import java.util.Optional;
import java.util.stream.Collectors;

@UIScope
@Component
@Route(value = "admin/materials/id")
@Log4j2
public class MaterialTemplateEditPage extends EditPage<MaterialTemplate> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final ComboBox<MeasurementUnit> measurementUnitField = new ComboBox<>();
    private final Checkbox activeField = new Checkbox();
    private final Checkbox perJobField = new Checkbox();
    private final TextField pricePerUnitField = new TextField();
    private final PriceField<MaterialPrice, JobTemplate> individualPricesField = new PriceField<>(MaterialPrice.class);

    private final Label nameLabel = new Label();
    private final Label measurementUnitLabel = new Label();
    private final Label pricePerUnitLabel = new Label();
    private final Label activeLabel = new Label();
    private final Label perJobLabel = new Label();
    private final Label individualPricesLabel = new Label();

    public MaterialTemplateEditPage(final DataService dataService) {
        super(dataService, "Editare Material");

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

        final Optional<MaterialTemplate> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, MaterialTemplate.class);
        } else {
            item = Optional.of(new MaterialTemplate());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(MaterialTemplatesPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        measurementUnitLabel.setText(Localizer.getLocalizedString("measurementUnit"));
        pricePerUnitLabel.setText(Localizer.getLocalizedString("pricePerUnit"));
        activeLabel.setText(Localizer.getLocalizedString("active"));
        perJobLabel.setText(Localizer.getLocalizedString("isPerJob"));
        individualPricesLabel.setText(Localizer.getLocalizedString("individualPrices"));
    }

    protected void reload() {
        measurementUnitField.setItems(MeasurementUnit.values());
        individualPricesField.setOptions(dataService.getAll(JobTemplate.class)
                .stream().filter(JobTemplate::isActive).collect(Collectors.toList()));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(measurementUnitField, measurementUnitLabel);
        generalLayout.addFormItem(perJobField, perJobLabel);
        generalLayout.addFormItem(individualPricesField, individualPricesLabel).getStyle().set("align-items", "end");
        generalLayout.addFormItem(pricePerUnitField, pricePerUnitLabel);
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        measurementUnitField.addClassName("dento-form-field");
        pricePerUnitField.addClassName("dento-form-field");
        activeField.addClassName("dento-form-field");
        perJobField.addClassName("dento-form-field");
        measurementUnitField.setItemLabelGenerator(val -> Localizer.getLocalizedString(val.name().toLowerCase()));
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired("Introduceți nume!")
                .bind(MaterialTemplate::getName, MaterialTemplate::setName);

        binder.forField(measurementUnitField)
                .asRequired("Introduceți unitate de măsură!")
                .bind(MaterialTemplate::getMeasurementUnit, MaterialTemplate::setMeasurementUnit);

        binder.forField(pricePerUnitField)
                .withConverter(new StringToFloatConverter("Introduceți o valoare!"))
                .withValidator(new FloatRangeValidator("Introduceți o valoare între 0 și 100000!", 0f, 100000f))
                .bind(MaterialTemplate::getPricePerUnit, MaterialTemplate::setPricePerUnit);

        binder.forField(activeField)
                .bind(MaterialTemplate::isActive, MaterialTemplate::setActive);

        binder.forField(perJobField)
                .bind(MaterialTemplate::isPerJob, MaterialTemplate::setPerJob);

        binder.forField(individualPricesField)
                .bind(MaterialTemplate::getIndividualPrices, MaterialTemplate::setIndividualPrices);

    }

    protected void save() {
        final BinderValidationStatus<MaterialTemplate> status = binder.validate();

        if (!status.hasErrors()) {
            final MaterialTemplate item = binder.getBean();
            dataService.saveEntity(item);
            DentoNotification.success("Datele s-au salvat cu success!");
            UI.getCurrent().navigate(MaterialTemplatesPage.class);
        } else {
            DentoNotification.error("Date invalide!",
                    status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.toList()));
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(MaterialTemplatesPage.class);
    }
}
