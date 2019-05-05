package com.company.dento.ui.page.edit;

import com.company.dento.model.business.Clinic;
import com.company.dento.model.business.Doctor;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.ClinicsPage;
import com.company.dento.ui.page.list.DoctorsPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
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
@Route(value = "doctors/id")
@Log4j2
public class DoctorEditPage extends EditPage<Doctor> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final ComboBox<Clinic> clinicField = new ComboBox<>();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();

    private final Label firstNameLabel = new Label();
    private final Label lastNameLabel = new Label();
    private final Label clinicLabel = new Label();
    private final Label emailLabel = new Label();
    private final Label phoneLabel = new Label();

    public DoctorEditPage(final DataService dataService) {
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

        final Optional<Doctor> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, Doctor.class);
        } else {
            item = Optional.of(new Doctor());
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
        firstNameLabel.setText(Localizer.getLocalizedString("firstName"));
        lastNameLabel.setText(Localizer.getLocalizedString("lastName"));
        clinicLabel.setText(Localizer.getLocalizedString("clinic"));
        phoneLabel.setText(Localizer.getLocalizedString("phone"));
        emailLabel.setText(Localizer.getLocalizedString("email"));
    }

    protected void reload() {
        clinicField.setItems(dataService.getAll(Clinic.class));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(firstNameField, firstNameLabel);
        generalLayout.addFormItem(lastNameField, lastNameLabel);
        generalLayout.addFormItem(clinicField, clinicLabel);
        generalLayout.addFormItem(emailField, emailLabel);
        generalLayout.addFormItem(phoneField, phoneLabel);

        generalLayout.addClassName("dento-form-layout");
        firstNameField.addClassName("dento-form-field");
        lastNameField.addClassName("dento-form-field");
        clinicField.addClassName("dento-form-field");
        emailField.addClassName("dento-form-field");
        phoneField.addClassName("dento-form-field");

        clinicField.setItemLabelGenerator(Clinic::getName);

        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(firstNameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Doctor::getFirstName, Doctor::setFirstName);

        binder.forField(lastNameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Doctor::getLastName, Doctor::setLastName);

        binder.forField(clinicField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(Doctor::getClinic, Doctor::setClinic);

        binder.forField(emailField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .withValidator(new EmailValidator("Email invalid!"))
                .bind(Doctor::getEmail, Doctor::setEmail);

        binder.forField(phoneField)
                .bind(Doctor::getPhone, Doctor::setPhone);
    }

    protected void save() {
        if (binder.isValid()) {
            final Doctor item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(DoctorsPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(DoctorsPage.class);
    }
}
