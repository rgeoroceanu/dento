package com.company.dento.ui.page;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.company.dento.model.business.Sample;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.SampleLayout;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;

@Component
@UIScope
@SpringView
public class SamplePage extends Page {
	
	private static final long serialVersionUID = 1L;
	private final Map<String, ClickListener> navigationItems = new LinkedHashMap<>();
	private final SampleLayout sampleLayout;
	private final Binder<Sample> binder;
	
	public SamplePage() {
		sampleLayout = new SampleLayout();
		binder = new Binder<>(Sample.class);
		sampleLayout.addSaveButtonListener(e -> handleSave());
		sampleLayout.addRemoveButtonListener(e -> handleRemove());
		this.setLayout(sampleLayout);
		bindFields();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
		final String parameters = event.getParameters();
		final Sample sample = extractFromParameters(parameters);
		open(sample);	
	}

	private void open(Sample sample) {
		initNavigationItems();
		if (sample == null) {
			sample = new Sample();
		} else {
			navigationItems.put(sample.getId().toString(), e -> {});
		}
		setReadOnlyFields(sample);
		sampleLayout.setNavigationItems(navigationItems);
		binder.setBean(sample);
	}

	private void bindFields() {
		binder.forField(sampleLayout.getSampleOverviewLayout().getSampleTypeField())
			.asRequired("Please choose sample type!")
			.bind(Sample::getTemplate, Sample::setTemplate);
		binder.forField(sampleLayout.getSampleOverviewLayout().getProcedureField())
			.asRequired("Please choose procedure!")
			.bind(Sample::getProcedure, Sample::setProcedure);
		binder.forField(sampleLayout.getSampleOverviewLayout().getDateField())
			.asRequired("Please set date!")
			.bind(Sample::getDate, Sample::setDate);
		binder.forField(sampleLayout.getSampleOverviewLayout().getDescriptionField())
			.bind(Sample::getDescription, Sample::setDescription);

	}
	
	private void setReadOnlyFields(Sample sample) {
		if (sample.getId() == null) {
			sampleLayout.getSampleOverviewLayout().getProcedureField().setReadOnly(false);
			sampleLayout.getSampleOverviewLayout().getSampleTypeField().setReadOnly(false);
		} else {
			sampleLayout.getSampleOverviewLayout().getProcedureField().setReadOnly(true);
			sampleLayout.getSampleOverviewLayout().getSampleTypeField().setReadOnly(true);
		}
	}
	
	private void handleSave() {
		if (binder.isValid() == false) {
			Notification.show(Localizer.getLocalizedString("error_save_data"));
			return;
		}
		final Sample sample = binder.getBean();
		dataService.saveEntity(sample);
		Notification.show(Localizer.getLocalizedString("saved"));
	}
	
	private void handleRemove() {
		final Sample sample = binder.getBean();
		if (sample != null && sample.getId() != null) {
			ConfirmDialog.show(this.getUI(), 
					Localizer.getLocalizedString("delete"), 
					Localizer.getLocalizedString("confirm_remove_message"), 
					Localizer.getLocalizedString("delete"), 
					Localizer.getLocalizedString("cancel"), confirmEvent -> {
						if (confirmEvent.isConfirmed()) {
							confirmRemove(sample);
						}
					});
		}
	}
	
	private void initNavigationItems() {
		navigationItems.clear();
		navigationItems.put("executions", e -> DentoUI.getCurrent().navigateToExecutionsPage());
	}
	
	private void confirmRemove(final Sample sample) {
		try {
			// remove entity
			dataService.deleteEntity(sample.getId(), Sample.class);

		} catch (DataDoesNotExistException e) {
			Notification.show(Localizer.getLocalizedString("error_remove_element"));
		}

		// reinitialize binder
		binder.readBean(binder.getBean());

		// navigate to home page
		DentoUI.getCurrent().navigateToStartPage();
		Notification.show(Localizer.getLocalizedString("saved"));
	}
	
	private Sample extractFromParameters(final String parameters) {
		Sample sample = null;

		if (parameters == null || parameters.isEmpty()) {
			return sample;
		} 

		try {
			final Long id = Long.valueOf(parameters);
			sample = dataService.getEntity(id, Sample.class);
		} catch (NumberFormatException | DataDoesNotExistException e) {
			// do nothing	
		}

		return sample;
	}
}
