package com.company.dento.ui.page.edit;

import com.company.dento.model.business.ToothColor;
import com.company.dento.model.type.ColorCategory;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.ToothColorsPage;
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
@Route(value = "admin/tooth_colors/id")
@Log4j2
public class ToothColorEditPage extends EditPage<ToothColor> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final ComboBox<ColorCategory> categoryField = new ComboBox<>();

    private final Label nameLabel = new Label();
    private final Label categoryLabel = new Label();

    public ToothColorEditPage(final DataService dataService) {
        super(dataService, "Culoare dinte");

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

        final Optional<ToothColor> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, ToothColor.class);
        } else {
            item = Optional.of(new ToothColor());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(ToothColorsPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        categoryLabel.setText(Localizer.getLocalizedString("category"));
    }

    protected void reload() {
        categoryField.setItems(ColorCategory.values());
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel).getStyle();
        generalLayout.addFormItem(categoryField, categoryLabel);
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        categoryField.addClassName("dento-form-field");
        categoryField.setAllowCustomValue(false);
        categoryField.setPreventInvalidInput(true);
        final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
        final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("500px", 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE);
        generalLayout.setResponsiveSteps(rs1, rs2);
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(ToothColor::getName, ToothColor::setName);

        binder.forField(categoryField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(ToothColor::getCategory, ToothColor::setCategory);

    }

    protected void save() {
        if (binder.isValid()) {
            final ToothColor item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(ToothColorsPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(ToothColorsPage.class);
    }
}
