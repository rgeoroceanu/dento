package com.company.dento.ui.page.edit;

import com.company.dento.model.business.Clinic;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.DentoNotification;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.ClinicsPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.EmailValidator;
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
@Route(value = "admin/clinics/id")
@Log4j2
public class ClinicEditPage extends EditPage<Clinic> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final TextField townField = new TextField();
    private final TextArea addressField = new TextArea();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();

    private final Label nameLabel = new Label();
    private final Label townLabel = new Label();
    private final Label addressLabel = new Label();
    private final Label emailLabel = new Label();
    private final Label phoneLabel = new Label();

    public ClinicEditPage(final DataService dataService) {
            super(dataService, "Clinică");

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

        final Optional<Clinic> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, Clinic.class);
        } else {
            item = Optional.of(new Clinic());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(ClinicsPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        townLabel.setText(Localizer.getLocalizedString("town"));
        addressLabel.setText(Localizer.getLocalizedString("address"));
        phoneLabel.setText(Localizer.getLocalizedString("phone"));
        emailLabel.setText(Localizer.getLocalizedString("email"));
    }

    protected void reload() { }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel);
        generalLayout.addFormItem(emailField, emailLabel);
        generalLayout.addFormItem(phoneField, phoneLabel);
        generalLayout.addFormItem(townField, townLabel);
        generalLayout.addFormItem(addressField, addressLabel);

        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        addressField.addClassName("dento-form-field");
        townField.addClassName("dento-form-field");
        emailField.addClassName("dento-form-field");
        phoneField.addClassName("dento-form-field");

        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired("Introduceți nume!")
                .bind(Clinic::getName, Clinic::setName);

        binder.forField(townField)
                .asRequired("Introduceți oraș!")
                .bind(Clinic::getTown, Clinic::setTown);

        binder.forField(addressField)
                .bind(Clinic::getAddress, Clinic::setAddress);

        binder.forField(emailField)
                .asRequired("Introduceți email!")
                .withValidator(new EmailValidator("Email invalid!"))
                .bind(Clinic::getEmail, Clinic::setEmail);

        binder.forField(phoneField)
                .bind(Clinic::getPhone, Clinic::setPhone);
    }

    protected void save() {
        final BinderValidationStatus<Clinic> status = binder.validate();

        if (!status.hasErrors()) {
            final Clinic item = binder.getBean();
            dataService.saveEntity(item);
            DentoNotification.success("Clinica s-a salvat cu success!");
            UI.getCurrent().navigate(ClinicsPage.class);
        } else {
            DentoNotification.error("Date invalide!",
                    status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.toList()));
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(ClinicsPage.class);
    }
}
