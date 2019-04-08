package com.company.dento.ui.page.edit;

import com.company.dento.model.business.JobTemplate;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.model.type.SelectionType;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.TwinColSelect;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.JobTemplatesPage;
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
@Route(value = "jobTemplate")
@Log4j2
public class JobTemplateEditPage extends EditPage<JobTemplate> {

    private final FormLayout generalLayout = new FormLayout();
    private final TextField nameField = new TextField();
    private final ComboBox<SelectionType> selectionTypeField = new ComboBox<>();
    private final Checkbox activeField = new Checkbox();
    private final TextField standardPriceField = new TextField();
    private final TwinColSelect<SampleTemplate> sampleTemplatesField = new TwinColSelect<>(SampleTemplate.class, "name");

    private final Label nameLabel = new Label();
    private final Label selectionTypeLabel = new Label();
    private final Label standardPriceLabel = new Label();
    private final Label activeLabel = new Label();
    private final Label sampleTemplatesLabel = new Label();

    public JobTemplateEditPage(final DataService dataService) {
        super(dataService);

        initGeneralLayout();
        reload();
        bindFields();

        contentLayout.addComponentAtIndex(0, generalLayout);
        generalLayout.setSizeFull();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        reload();
    }

    @Override
    public void setParameter(final BeforeEvent beforeEvent, final @OptionalParameter Long itemId) {
        reload();

        final Optional<JobTemplate> item;
        if (itemId != null) {
            item = dataService.getEntity(itemId, JobTemplate.class);
        } else {
            item = Optional.of(new JobTemplate());
        }

        if (item.isPresent()) {
            binder.setBean(item.get());
        } else {
            beforeEvent.rerouteTo(JobTemplatesPage.class);
        }
    }

    @Override
    public void localize() {
        super.localize();
        nameLabel.setText(Localizer.getLocalizedString("name"));
        selectionTypeLabel.setText(Localizer.getLocalizedString("selectionType"));
        standardPriceLabel.setText(Localizer.getLocalizedString("standardPrice"));
        activeLabel.setText(Localizer.getLocalizedString("active"));
        sampleTemplatesLabel.setText(Localizer.getLocalizedString("samples"));
    }

    protected void reload() {
        selectionTypeField.setItems(SelectionType.values());
        sampleTemplatesField.setOptions(dataService.getAll(SampleTemplate.class));
    }

    private void initGeneralLayout() {
        generalLayout.addFormItem(nameField, nameLabel);
        generalLayout.addFormItem(selectionTypeField, selectionTypeLabel);
        generalLayout.addFormItem(standardPriceField, standardPriceLabel);
        generalLayout.addFormItem(activeField, activeLabel);
        generalLayout.addFormItem(sampleTemplatesField, sampleTemplatesLabel);
        generalLayout.addClassName("dento-form-layout");
        nameField.addClassName("dento-form-field");
        selectionTypeField.addClassName("dento-form-field");
        standardPriceField.addClassName("dento-form-field");
        activeField.addClassName("dento-form-field");
        selectionTypeField.setItemLabelGenerator(val -> Localizer.getLocalizedString(val.name().toLowerCase()));
    }

    protected void bindFields() {
        binder.forField(nameField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(JobTemplate::getName, JobTemplate::setName);

        binder.forField(selectionTypeField)
                .asRequired(Localizer.getLocalizedString("requiredValidation"))
                .bind(JobTemplate::getSelectionType, JobTemplate::setSelectionType);

        binder.forField(standardPriceField)
                .withConverter(new StringToIntegerConverter(Localizer.getLocalizedString("integerRangeValidation")))
                .withValidator(new IntegerRangeValidator("integerRangeValidation", 0, 100000))
                .bind(JobTemplate::getStandardPrice, JobTemplate::setStandardPrice);

        binder.forField(activeField)
                .bind(JobTemplate::isActive, JobTemplate::setActive);

        binder.forField(sampleTemplatesField)
                .bind(JobTemplate::getSampleTemplates, JobTemplate::setSampleTemplates);

    }

    protected void save() {
        if (binder.isValid()) {
            final JobTemplate item = binder.getBean();
            dataService.saveEntity(item);
            UI.getCurrent().navigate(JobTemplatesPage.class);
        } else {
            Notification.show(Localizer.getLocalizedString("validationError"),
                    5000, Notification.Position.BOTTOM_CENTER);
        }
    }

    protected void discard() {
        UI.getCurrent().navigate(JobTemplatesPage.class);
    }
}
