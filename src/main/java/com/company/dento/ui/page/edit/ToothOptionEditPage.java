package com.company.dento.ui.page.edit;

import com.company.dento.model.business.JobTemplate;
import com.company.dento.model.business.ToothOption;
import com.company.dento.model.type.ToothOptionColumn;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.ToothOptionsPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UIScope
@Component
@Route(value = "admin/tooth_options/id")
@Log4j2
public class ToothOptionEditPage extends EditPage<ToothOption> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final TextField abbreviationField = new TextField();
    private final ComboBox<ToothOptionColumn> columnField = new ComboBox<>();
    private final ComboBox<JobTemplate> specificJobField = new ComboBox<>();

    private final Label nameLabel = new Label();
    private final Label abbreviationLabel = new Label();
    private final Label columnLabel = new Label();
    private final Label specificJobLabel = new Label();

    public ToothOptionEditPage(final DataService dataService) {
        super(dataService, "Opțiune dinte");

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

        final Optional<ToothOption> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, ToothOption.class);
        } else {
            item = Optional.of(new ToothOption());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(ToothOptionsPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        abbreviationLabel.setText(Localizer.getLocalizedString("abbreviation"));
        columnLabel.setText(Localizer.getLocalizedString("toothColumn"));
        specificJobLabel.setText(Localizer.getLocalizedString("specificJob"));
    }

    protected void reload() {
        columnField.setItems(ToothOptionColumn.values());
        specificJobField.setItems(dataService.getAll(JobTemplate.class));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(abbreviationField, abbreviationLabel);
        generalLayout.addFormItem(columnField, columnLabel);
        generalLayout.addFormItem(specificJobField, specificJobLabel);
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        abbreviationField.addClassName("dento-form-field");
        columnField.addClassName("dento-form-field");
        specificJobField.addClassName("dento-form-field");
        columnField.setAllowCustomValue(false);
        columnField.setPreventInvalidInput(true);
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(ToothOption::getName, ToothOption::setName);

        binder.forField(abbreviationField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(ToothOption::getAbbreviation, ToothOption::setAbbreviation);

        binder.forField(columnField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(ToothOption::getDisplayColumn, ToothOption::setDisplayColumn);

        binder.forField(specificJobField)
                .bind(ToothOption::getSpecificJob, ToothOption::setSpecificJob);

    }

    protected void save() {
        if (binder.isValid()) {
            final ToothOption item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(ToothOptionsPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(ToothOptionsPage.class);
    }
}
