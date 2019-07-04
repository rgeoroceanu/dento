package com.company.dento.ui.page.edit;

import com.company.dento.model.business.Material;
import com.company.dento.model.type.MeasurementUnit;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.MaterialsPage;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;

import java.util.Optional;

//@UIScope
//@Component
@Secured(value = {"USER", "ADMIN"})
@Route(value = "materials/id")
@Log4j2
public class MaterialEditPage extends EditPage<Material> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final ComboBox<MeasurementUnit> measurementUnitField = new ComboBox<>();
    private final Checkbox activeField = new Checkbox();
    private final Checkbox perJobField = new Checkbox();
    private final TextField pricePerUnitField = new TextField();
    //private final MaterialPriceField individualPricesField = new MaterialPriceField();

    private final Label nameLabel = new Label();
    private final Label measurementUnitLabel = new Label();
    private final Label pricePerUnitLabel = new Label();
    private final Label activeLabel = new Label();
    private final Label perJobLabel = new Label();
    private final Label individualPricesLabel = new Label();

    public MaterialEditPage(final DataService dataService) {
        super(dataService);

        initGeneralLayout();
        reload();
        bindFields();

        contentLayout.addComponentAtIndex(0, generalLayout);
        generalLayout.setSizeFull();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    }

    @Override
    public void setParameter(final BeforeEvent beforeEvent, final @OptionalParameter Long itemId) {
        reload();

        final Optional<Material> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, Material.class);
        } else {
            item = Optional.of(new Material());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(MaterialsPage.class);
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
        //individualPricesField.setJobs(dataService.getAll(JobTemplate.class));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(measurementUnitField, measurementUnitLabel);
        generalLayout.addFormItem(perJobField, perJobLabel);
        //generalLayout.addFormItem(individualPricesField, individualPricesLabel).getStyle().set("align-items", "end");
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
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Material::getName, Material::setName);

        binder.forField(measurementUnitField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Material::getMeasurementUnit, Material::setMeasurementUnit);

        binder.forField(pricePerUnitField)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("integerRangeValidation", 0, 100000))
                .bind(Material::getPricePerUnit, Material::setPricePerUnit);

        binder.forField(activeField)
                .bind(Material::isActive, Material::setActive);

        binder.forField(perJobField)
                .bind(Material::isPerJob, Material::setPerJob);

        //binder.forField(individualPricesField)
        //        .bind(Material::getIndividualPrices, Material::setIndividualPrices);

    }

    protected void save() {
        if (binder.isValid()) {
            final Material item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(MaterialsPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(MaterialsPage.class);
    }
}
