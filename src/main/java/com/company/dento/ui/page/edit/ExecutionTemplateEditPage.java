package com.company.dento.ui.page.edit;

import com.company.dento.model.business.ExecutionPrice;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.PriceField;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.ExecutionTemplatesPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
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
@Route(value = "jobTemplates/id")
@Log4j2
public class ExecutionTemplateEditPage extends EditPage<ExecutionTemplate> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final Checkbox activeField = new Checkbox();
    private final TextField standardPriceField = new TextField();
    private final PriceField<ExecutionPrice, User> individualPricesField = new PriceField<>(ExecutionPrice.class);

    private final Label nameLabel = new Label();
    private final Label standardPriceLabel = new Label();
    private final Label activeLabel = new Label();
    private final Label individualPricesLabel = new Label();

    public ExecutionTemplateEditPage(final DataService dataService) {
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

        final Optional<ExecutionTemplate> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, ExecutionTemplate.class);
        } else {
            item = Optional.of(new ExecutionTemplate());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(ExecutionTemplatesPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        standardPriceLabel.setText(Localizer.getLocalizedString("standardPrice"));
        activeLabel.setText(Localizer.getLocalizedString("active"));
        individualPricesLabel.setText(Localizer.getLocalizedString("individualPrices"));
    }

    protected void reload() {
        individualPricesField.setOptions(dataService.getAllTechnicians());
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(individualPricesField, individualPricesLabel).getStyle().set("align-items", "end");
        generalLayout.addFormItem(standardPriceField, standardPriceLabel);
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        standardPriceField.addClassName("dento-form-field");
        activeField.addClassName("dento-form-field");
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(ExecutionTemplate::getName, ExecutionTemplate::setName);

        binder.forField(standardPriceField)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("integerRangeValidation", 0, 100000))
                .bind(ExecutionTemplate::getStandardPrice, ExecutionTemplate::setStandardPrice);

        binder.forField(activeField)
                .bind(ExecutionTemplate::isActive, ExecutionTemplate::setActive);


        binder.forField(individualPricesField)
                .bind(ExecutionTemplate::getIndividualPrices, ExecutionTemplate::setIndividualPrices);

    }

    protected void save() {
        if (binder.isValid()) {
            final ExecutionTemplate item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(ExecutionTemplatesPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(ExecutionTemplatesPage.class);
    }
}
