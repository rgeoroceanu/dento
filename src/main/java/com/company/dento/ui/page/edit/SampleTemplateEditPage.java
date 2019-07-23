package com.company.dento.ui.page.edit;

import com.company.dento.model.business.MaterialTemplate;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.DentoNotification;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.SampleTemplatesPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
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
@Route(value = "admin/samples/id")
@Log4j2
public class SampleTemplateEditPage extends EditPage<SampleTemplate> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final Checkbox activeField = new Checkbox();

    private final Label nameLabel = new Label();
    private final Label activeLabel = new Label();

    public SampleTemplateEditPage(final DataService dataService) {
        super(dataService, "Probă");

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

        final Optional<SampleTemplate> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, SampleTemplate.class);
        } else {
            item = Optional.of(new SampleTemplate());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(SampleTemplatesPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        activeLabel.setText(Localizer.getLocalizedString("active"));
    }

    protected void reload() { }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        activeField.addClassName("dento-form-field");
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired("Introduceți nume!")
                .bind(SampleTemplate::getName, SampleTemplate::setName);

        binder.forField(activeField)
                .bind(SampleTemplate::isActive, SampleTemplate::setActive);

    }

    protected void save() {
        final BinderValidationStatus<SampleTemplate> status = binder.validate();

        if (!status.hasErrors()) {
            final SampleTemplate item = binder.getBean();
            dataService.saveEntity(item);
            DentoNotification.success("Datele s-au salvat cu success!");
            UI.getCurrent().navigate(SampleTemplatesPage.class);
        } else {
            DentoNotification.error("Date invalide!",
                    status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.toList()));
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(SampleTemplatesPage.class);
    }
}
