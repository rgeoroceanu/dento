package com.company.dento.ui.page.edit;

import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.GeneralData;
import com.company.dento.model.type.Currency;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.DentoNotification;
import com.company.dento.ui.component.common.UploadField;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
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

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@UIScope
@Component
@Route(value = "admin/general")
@Log4j2
public class GeneralDataEditPage extends EditPage<GeneralData> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField laboratoryNameField = new TextField();
    private final TextField townField = new TextField();
    private final TextArea addressField = new TextArea();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField postalCodeField = new TextField();
    private final UploadField logoField = new UploadField();
    private final TextArea reportEmailField = new TextArea();
    private final ComboBox<Currency> currencyField = new ComboBox<>();

    private final Label laboratoryNameLabel = new Label();
    private final Label townLabel = new Label();
    private final Label addressLabel = new Label();
    private final Label emailLabel = new Label();
    private final Label phoneLabel = new Label();
    private final Label postalCodeLabel = new Label();
    private final Label logoLabel = new Label();
    private final Label reportEmailLabel = new Label();
    private final Label currencyLabel = new Label();

    public GeneralDataEditPage(final DataService dataService) {
        super(dataService, "Date generale");

        initGeneralLayout();
        reload();
        bindFields();

        contentLayout.addComponentAtIndex(0, generalLayout);
        generalLayout.setSizeFull();

        this.disableDiscardButton();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        super.afterNavigation(afterNavigationEvent);
    }

    @Override
    public void setParameter(final BeforeEvent beforeEvent, final @OptionalParameter Long itemId) {
        reload();

        final Optional<GeneralData> item = dataService.getGeneralData();

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            binder.setBean(new GeneralData());
        }
    }

    @Override
    public void localize() {
        super.localize();
        laboratoryNameLabel.setText(Localizer.getLocalizedString("laboratoryName"));
        townLabel.setText(Localizer.getLocalizedString("town"));
        addressLabel.setText(Localizer.getLocalizedString("address"));
        phoneLabel.setText(Localizer.getLocalizedString("phone"));
        emailLabel.setText(Localizer.getLocalizedString("email"));
        postalCodeLabel.setText(Localizer.getLocalizedString("postalCode"));
        logoLabel.setText(Localizer.getLocalizedString("logo"));
        reportEmailLabel.setText(Localizer.getLocalizedString("reportEmailText"));
        currencyLabel.setText(Localizer.getLocalizedString("currency"));
    }

    protected void reload() {
        currencyField.setItems(Currency.values());
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(laboratoryNameField, laboratoryNameLabel);
        generalLayout.addFormItem(logoField, logoLabel);
        generalLayout.addFormItem(addressField, addressLabel);
        generalLayout.addFormItem(townField, townLabel);
        generalLayout.addFormItem(postalCodeField, postalCodeLabel);
        generalLayout.addFormItem(emailField, emailLabel);
        generalLayout.addFormItem(phoneField, phoneLabel);
        generalLayout.addFormItem(currencyField, currencyLabel);
        generalLayout.addFormItem(reportEmailField, reportEmailLabel);

        generalLayout.addClassName("dento-form-layout");
        laboratoryNameField.addClassName("dento-form-field");
        addressField.addClassName("dento-form-field");
        townField.addClassName("dento-form-field");
        emailField.addClassName("dento-form-field");
        phoneField.addClassName("dento-form-field");
        postalCodeField.addClassName("dento-form-field");
        //logoField.addClassName("dento-form-field");
        currencyField.addClassName("dento-form-field");
        reportEmailField.addClassName("dento-form-field");
        reportEmailField.setHeight("10em");
        logoField.setMaxFiles(1);

        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(laboratoryNameField)
                .asRequired("Introduceți nume!")
                .bind(GeneralData::getLaboratoryName, GeneralData::setLaboratoryName);

        binder.forField(townField)
                .asRequired("Introduceți oraș!")
                .bind(GeneralData::getTown, GeneralData::setTown);

        binder.forField(addressField)
                .bind(GeneralData::getAddress, GeneralData::setAddress);

        binder.forField(emailField)
                .asRequired("Introduceți email!")
                .withValidator(new EmailValidator("Email invalid!"))
                .bind(GeneralData::getEmail, GeneralData::setEmail);

        binder.forField(phoneField)
                .bind(GeneralData::getPhone, GeneralData::setPhone);

        binder.forField(postalCodeField)
                .bind(GeneralData::getPostalCode, GeneralData::setPostalCode);

        binder.forField(logoField)
                .bind(gd -> gd.getLogo() != null ? Collections.singleton(gd.getLogo()) : Collections.emptySet(),
                        (gd, value) -> gd.setLogo(value.stream().findFirst().orElse(null)));

        binder.forField(currencyField)
                .asRequired("Alegeți moneda!")
                .bind(GeneralData::getCurrency, GeneralData::setCurrency);
    }

    protected void save() {
        final BinderValidationStatus<GeneralData> status = binder.validate();

        if (!status.hasErrors()) {
            final GeneralData item = binder.getBean();
            dataService.saveGeneralData(item);
            DentoNotification.success("Datele s-au salvat cu success!");
        } else {
            DentoNotification.error("Date invalide!",
                    status.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.toList()));
        }
    }

    protected void discard() {
    }
}
