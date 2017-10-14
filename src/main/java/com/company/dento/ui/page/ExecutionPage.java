package com.company.dento.ui.page;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.Procedure;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.ExecutionLayout;
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
		executionLayout.getExecutionOverviewLayout().getExecutionTypeField().addValueChangeListener(e -> 
			updateTemplateFields((ExecutionTemplate) e.getValue()));
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
			initSelectionFields();
		} else {
			navigationItems.put(execution.getId().toString(), e -> {});
		}
		setReadOnlyFields(execution);
		executionLayout.setNavigationItems(navigationItems);
		binder.setBean(execution);
	}

	private void bindFields() {
		binder.forField(executionLayout.getExecutionOverviewLayout().getExecutionTypeField())
			.asRequired("Please choose execution type!")
			.bind(Execution::getTemplate, Execution::setTemplate);
		binder.forField(executionLayout.getExecutionOverviewLayout().getProcedureField())
			.asRequired("Please choose procedure!")
			.bind(Execution::getProcedure, Execution::setProcedure);
		binder.forField(executionLayout.getExecutionOverviewLayout().getTechnicianField())
			.asRequired("Please assign technician!")
			.bind(Execution::getTechnician, Execution::setTechnician);
		binder.forField(executionLayout.getExecutionOverviewLayout().getPriceField())
			.asRequired("Please set price!")
			.withConverter(new StringToIntegerConverter("Number required"))
			.bind(Execution::getPrice, Execution::setPrice);
		binder.forField(executionLayout.getExecutionOverviewLayout().getStatusField())
			.asRequired("Please set status!")
			.bind(Execution::getStatus, Execution::setStatus);
		binder.forField(executionLayout.getExecutionOverviewLayout().getPriorityField())
			.asRequired("Please set priority!")
			.bind(Execution::getPriority, Execution::setPriority);
		binder.forField(executionLayout.getExecutionOverviewLayout().getEstimatedDurationField())
			.withConverter(new StringToIntegerConverter("Number required"))
			.bind(Execution::getEstimatedDuration, Execution::setEstimatedDuration);
		binder.forField(executionLayout.getExecutionOverviewLayout().getDeadlineDateField())
			.bind(Execution::getDeadlineDate, Execution::setDeadlineDate);
		binder.forField(executionLayout.getExecutionOverviewLayout().getSpentTimeField())
			.withConverter(new StringToIntegerConverter("Number required"))
			.bind(Execution::getSpentTime, Execution::setSpentTime);
		binder.forField(executionLayout.getExecutionOverviewLayout().getProgressPercentageField())
			.bind(Execution::getProgressPercentage, Execution::setProgressPercentage);
		binder.forField(executionLayout.getExecutionOverviewLayout().getDescriptionField())
			.bind(Execution::getDescription, Execution::setDescription);
	}
	
	private void initSelectionFields() {
		executionLayout.getExecutionOverviewLayout().getExecutionTypeField().setItems(dataService.getAll(ExecutionTemplate.class));
		executionLayout.getExecutionOverviewLayout().getProcedureField().setItems(dataService.getAll(Procedure.class));
	}
	
	private void updateTemplateFields(ExecutionTemplate executionTemplate) {
		final Execution execution = binder.getBean();
		if (execution.getId() == null && executionTemplate != null) {
			executionLayout.getExecutionOverviewLayout().getPriceField().setValue(String.valueOf(executionTemplate.getPrice()));
			executionLayout.getExecutionOverviewLayout().getEstimatedDurationField().setValue(String.valueOf(executionTemplate.getEstimatedDuration()));
			executionLayout.getExecutionOverviewLayout().getPriorityField().setValue(executionTemplate.getPriority());
		}
	}
	
	private void setReadOnlyFields(Execution execution) {
		if (execution.getId() == null) {
			executionLayout.getExecutionOverviewLayout().getProcedureField().setReadOnly(false);
			executionLayout.getExecutionOverviewLayout().getExecutionTypeField().setReadOnly(false);
		} else {
			executionLayout.getExecutionOverviewLayout().getProcedureField().setReadOnly(true);
			executionLayout.getExecutionOverviewLayout().getExecutionTypeField().setReadOnly(true);
		}
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
