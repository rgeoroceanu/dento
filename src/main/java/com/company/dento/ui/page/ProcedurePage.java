package com.company.dento.ui.page;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.company.dento.model.business.Procedure;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.ProcedureLayout;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;

@Component
@UIScope
@SpringView
public class ProcedurePage extends Page {
	
	private static final long serialVersionUID = 1L;
	private final Map<String, ClickListener> navigationItems = new LinkedHashMap<>();
	private final ProcedureLayout procedureLayout;
	private final Binder<Procedure> binder;
	
	public ProcedurePage() {
		procedureLayout = new ProcedureLayout();
		binder = new Binder<>(Procedure.class);
		procedureLayout.addSaveButtonListener(e -> handleSave());
		procedureLayout.addRemoveButtonListener(e -> handleRemove());
		this.setLayout(procedureLayout);
		
		bindFields();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
		final String parameters = event.getParameters();
		final Procedure procedure = extractFromParameters(parameters);
		open(procedure);	
	}

	private void open(Procedure procedure) {
		initNavigationItems();
		if (procedure == null) {
			procedure = new Procedure();
		} else {
			navigationItems.put(procedure.getId().toString(), e -> {});
		}
		procedureLayout.setNavigationItems(navigationItems);
		binder.setBean(procedure);
		procedureLayout.getProcedureSamplesLayout().setItems(dataService.getProcedureSamples(procedure.getId()));
		procedureLayout.getProcedureExecutionsLayout().setItems(dataService.getProcedureExecutions(procedure.getId()));
	}

	private void bindFields() {
		binder.forField(procedureLayout.getProcedureOverviewLayout().getProcedureTypeField())
			.asRequired("Please choose procedure type!")
			.bind(Procedure::getTemplate, Procedure::setTemplate);
		binder.forField(procedureLayout.getProcedureOverviewLayout().getPriceField())
			.asRequired("Please insert price!")
			.withConverter(new StringToIntegerConverter("Number required"))
			.bind(Procedure::getPrice, Procedure::setPrice);
		binder.forField(procedureLayout.getProcedureOverviewLayout().getDoctorField())
			.asRequired("Please choose doctor!")
			.bind(Procedure::getDoctor, Procedure::setDoctor);
		binder.forField(procedureLayout.getProcedureOverviewLayout().getPatientField())
			.asRequired("Please provide patient name!")
			.bind(Procedure::getPatient, Procedure::setPatient);
		binder.forField(procedureLayout.getProcedureOverviewLayout().getDescriptionField())
			.bind(Procedure::getDescription, Procedure::setDescription);
		binder.forField(procedureLayout.getProcedureOverviewLayout().getDeliveryDateField())
			.bind(Procedure::getDeliveryDate, Procedure::setDeliveryDate);
	}
	
	private void handleSave() {
		if (binder.isValid() == false) {
			Notification.show(Localizer.getLocalizedString("error_save_data"));
			return;
		}
		final Procedure procedure = binder.getBean();
		dataService.saveEntity(procedure);
		Notification.show(Localizer.getLocalizedString("saved"));
	}
	
	private void handleRemove() {
		final Procedure procedure = binder.getBean();
		if (procedure != null && procedure.getId() != null) {
			ConfirmDialog.show(this.getUI(), 
					Localizer.getLocalizedString("delete"), 
					Localizer.getLocalizedString("confirm_remove_message"), 
					Localizer.getLocalizedString("delete"), 
					Localizer.getLocalizedString("cancel"), confirmEvent -> {
						if (confirmEvent.isConfirmed()) {
							confirmRemove(procedure);
						}
					});
		}
	}
	
	private void initNavigationItems() {
		navigationItems.clear();
		navigationItems.put("procedures", e -> DentoUI.getCurrent().navigateToProceduresPage());
	}
	
	private void confirmRemove(final Procedure procedure) {
		try {
			// remove entity
			dataService.deleteEntity(procedure.getId(), Procedure.class);

		} catch (DataDoesNotExistException e) {
			Notification.show(Localizer.getLocalizedString("error_remove_element"));
		}

		// reinitialize binder
		binder.readBean(binder.getBean());

		// navigate to home page
		DentoUI.getCurrent().navigateToStartPage();
		Notification.show(Localizer.getLocalizedString("saved"));
	}
	
	private Procedure extractFromParameters(final String parameters) {
		Procedure procedure = null;

		if (parameters == null || parameters.isEmpty()) {
			return procedure;
		} 

		try {
			final Long id = Long.valueOf(parameters);
			procedure = dataService.getEntity(id, Procedure.class);
		} catch (NumberFormatException | DataDoesNotExistException e) {
			// do nothing	
		}

		return procedure;
	}
}
