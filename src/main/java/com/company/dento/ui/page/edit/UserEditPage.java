package com.company.dento.ui.page.edit;

import com.company.dento.model.business.User;
import com.company.dento.model.type.Role;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.UsersPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UIScope
@Component
@Route(value = "admin/users/id")
@Log4j2
public class UserEditPage extends EditPage<User> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final MultiselectComboBox<Role> rolesField = new MultiselectComboBox<>();
    private final Checkbox activeField = new Checkbox();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();


    private final Label firstNameLabel = new Label();
    private final Label lastNameLabel = new Label();
    private final Label rolesLabel = new Label();
    private final Label activeLabel = new Label();
    private final Label usernameLabel = new Label();
    private final Label passwordLabel = new Label();

    public UserEditPage(final DataService dataService) {
        super(dataService, "Utilizator");

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

        final Optional<User> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, User.class);
        } else {
            item = Optional.of(new User());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(UsersPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        firstNameLabel.setText(Localizer.getLocalizedString("firstName"));
        lastNameLabel.setText(Localizer.getLocalizedString("lastName"));
        rolesLabel.setText(Localizer.getLocalizedString("roles"));
        activeLabel.setText(Localizer.getLocalizedString("active"));
        usernameLabel.setText(Localizer.getLocalizedString("username"));
        passwordLabel.setText(Localizer.getLocalizedString("password"));
    }

    protected void reload() {
        rolesField.setItems(Role.values());
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(firstNameField, firstNameLabel);
        generalLayout.addFormItem(lastNameField, lastNameLabel);
        generalLayout.addFormItem(rolesField, rolesLabel).getStyle().set("align-items", "end");;
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(usernameField, usernameLabel);
        generalLayout.addFormItem(passwordField, passwordLabel);
        generalLayout.addClassName("dento-form-layout");
        firstNameField.addClassName("dento-form-field");
        lastNameField.addClassName("dento-form-field");
        rolesField.addClassName("dento-form-field");
        activeField.addClassName("dento-form-field");
        usernameField.addClassName("dento-form-field");
        passwordField.addClassName("dento-form-field");
        rolesField.setItemLabelGenerator(val -> Localizer.getLocalizedString(val.name().toLowerCase()));
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(firstNameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(User::getFirstName, User::setFirstName);

        binder.forField(lastNameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(User::getLastName, User::setLastName);

        binder.forField(rolesField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(User::getRoles, User::setRoles);

        binder.forField(activeField)
                .bind(User::isActive, User::setActive);

        binder.forField(usernameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .withValidator(new StringLengthValidator("Minimum 8, maxim 16 caractere!", 8, 16))
                .bind(User::getUsername, User::setUsername);

        binder.forField(passwordField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .withValidator(new StringLengthValidator("Minimum 8, maxim 16 caractere!", 8, 16))
                .bind(User::getPassword, User::setPassword);

    }

    protected void save() {
        if (binder.isValid()) {
            final User item = binder.getBean();
            dataService.saveUserAndEncodePassword(item);
            UI.getCurrent().navigate(UsersPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(UsersPage.class);
    }
}
