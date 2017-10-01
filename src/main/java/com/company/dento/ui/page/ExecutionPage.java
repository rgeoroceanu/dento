package com.company.dento.ui.page;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.company.dento.model.business.Execution;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.ExecutionLayout;
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
public class ExecutionPage extends Page {
	
	private static final long serialVersionUID = 1L;
	private final Map<String, ClickListener> navigationItems = new LinkedHashMap<>();
	private final ExecutionLayout executionLayout;
	private final Binder<Execution> binder;
	
	public ExecutionPage() {
		executionLayout = new ExecutionLayout();
		binder = new Binder<>(Execution.class);
		executionLayout.addSaveButtonListener(e -> handleSave());
		executionLayout.addRemoveButtonListener(e -> handleRemove());
		this.setLayout(executionLayout);
		bindFields();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
		final String parameters = event.getParameters();
		final Execution execution = extractFromParameters(parameters);
		open(execution);	
	}

	private void open(Execution execution) {
		initNavigationItems();
		if (execution == null) {
			execution = new Execution();
		} else {
			navigationItems.put(execution.getId().toString(), e -> {});
		}
		executionLayout.setNavigationItems(navigationItems);
		binder.setBean(execution);
	}

	private void bindFields() {
//		binder.forField(procedureLayout.getProcedureOverviewLayout().getProcedureTypeField())
//			.asRequired("Please choose procedure type!")
//			.bind(Procedure::getTemplate, Procedure::setTemplate);
//		binder.forField(procedureLayout.getProcedureOverviewLayout().getPriceField())
//			.asRequired("Please insert price!")
//			.withConverter(new StringToIntegerConverter("Number required"))
//			.bind(Procedure::getPrice, Procedure::setPrice);
//		binder.forField(procedureLayout.getProcedureOverviewLayout().getDoctorField())
//			.asRequired("Please choose doctor!")
//			.bind(Procedure::getDoctor, Procedure::setDoctor);
//		binder.forField(procedureLayout.getProcedureOverviewLayout().getPatientField())
//			.asRequired("Please provide patient name!")
//			.bind(Procedure::getPatient, Procedure::setPatient);
//		binder.forField(procedureLayout.getProcedureOverviewLayout().getDescriptionField())
//			.bind(Procedure::getDescription, Procedure::setDescription);
//		binder.forField(procedureLayout.getProcedureOverviewLayout().getDeliveryDateField())
//			.bind(Procedure::getDeliveryDate, Procedure::setDeliveryDate);
	}
	
	private void handleSave() {
		if (binder.isValid() == false) {
			Notification.show(Localizer.getLocalizedString("error_save_data"));
			return;
		}
		final Execution execution = binder.getBean();
		dataService.saveEntity(execution);
		Notification.show(Localizer.getLocalizedString("saved"));
	}
	
	private void handleRemove() {
		final Execution execution = binder.getBean();
		if (execution != null && execution.getId() != null) {
			ConfirmDialog.show(this.getUI(), 
					Localizer.getLocalizedString("delete"), 
					Localizer.getLocalizedString("confirm_remove_message"), 
					Localizer.getLocalizedString("delete"), 
					Localizer.getLocalizedString("cancel"), confirmEvent -> {
						if (confirmEvent.isConfirmed()) {
							confirmRemove(execution);
						}
					});
		}
	}
	
	private void initNavigationItems() {
		navigationItems.clear();
		navigationItems.put("executions", e -> DentoUI.getCurrent().navigateToExecutionsPage());
	}
	
	private void confirmRemove(final Execution execution) {
		try {
			// remove entity
			dataService.deleteEntity(execution.getId(), Execution.class);

		} catch (DataDoesNotExistException e) {
			Notification.show(Localizer.getLocalizedString("error_remove_element"));
		}

		// reinitialize binder
		binder.readBean(binder.getBean());

		// navigate to home page
		DentoUI.getCurrent().navigateToStartPage();
		Notification.show(Localizer.getLocalizedString("saved"));
	}
	
	private Execution extractFromParameters(final String parameters) {
		Execution execution = null;

		if (parameters == null || parameters.isEmpty()) {
			return execution;
		} 

		try {
			final Long id = Long.valueOf(parameters);
			execution = dataService.getEntity(id, Execution.class);
		} catch (NumberFormatException | DataDoesNotExistException e) {
			// do nothing	
		}

		return execution;
	}
}
