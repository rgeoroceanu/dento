package com.company.dento.ui.layout;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.Priority;
import com.company.dento.model.business.Procedure;
import com.company.dento.model.business.Status;
import com.company.dento.model.business.User;
import com.company.dento.ui.component.form.Form;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import lombok.Getter;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@Getter
public class ExecutionOverviewLayout extends Form implements Localizable {

	private static final long serialVersionUID = 1L;
	
	private final ComboBox<Procedure> procedureField;
	private final ComboBox<ExecutionTemplate> executionTypeField;
	private final ComboBox<User> technicianField;
	private final TextField priceField;
	private final ComboBox<Priority> priorityField;
	private final ComboBox<Status> statusField;
	private final TextField estimatedDurationField;
	private final TextField spentTimeField;
	private final ComboBox<Integer> progressPercentageField;
	private final DateTimeField deadlineDateField;
	private final TextArea descriptionField;
	
	public ExecutionOverviewLayout() {
		procedureField = initProcedureField();
		executionTypeField = initExecutionTypeField();
		technicianField = initTechnicianField();
		priceField = initTextField();
		priorityField = initPriorityField();
		statusField = initStatusField();
		estimatedDurationField = initTextField();
		spentTimeField = initTextField();
		progressPercentageField = initProgressPercentageField();
		deadlineDateField = initDeadlineDateField();
		descriptionField = initDescriptionField();
		setupLayout();
	}
	
	@Override
	public void localize() {
		super.localize();
		procedureField.setCaption(Localizer.getLocalizedString("procedure"));
		priceField.setCaption(Localizer.getLocalizedString("price"));
		executionTypeField.setCaption(Localizer.getLocalizedString("type"));
		technicianField.setCaption(Localizer.getLocalizedString("technician"));
		priorityField.setCaption(Localizer.getLocalizedString("priority"));
		estimatedDurationField.setCaption(Localizer.getLocalizedString("estimated_duration"));
		descriptionField.setCaption(Localizer.getLocalizedString("description"));
		spentTimeField.setCaption(Localizer.getLocalizedString("spent_time"));
		progressPercentageField.setCaption(Localizer.getLocalizedString("progress") + " (%)");
		estimatedDurationField.setCaption(Localizer.getLocalizedString("estimated_duration"));
		deadlineDateField.setCaption(Localizer.getLocalizedString("deadline"));
		statusField.setCaption(Localizer.getLocalizedString("status"));
	}
	
	private void setupLayout() {
		this.addComponent(procedureField);
		this.addComponent(executionTypeField);
		this.addComponent(technicianField);
		this.addComponent(priceField);
		this.addComponent(statusField);
		this.addComponent(priorityField);
		this.addComponent(estimatedDurationField);
		this.addComponent(deadlineDateField);
		this.addComponent(spentTimeField);
		this.addComponent(progressPercentageField);
		this.addComponent(descriptionField);
		this.setMargin(true);
	}
	
	private ComboBox<Procedure> initProcedureField() {
		ComboBox<Procedure> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setWidth(25, Unit.EM);
		return comboBox;
	}
	
	private ComboBox<ExecutionTemplate> initExecutionTypeField() {
		ComboBox<ExecutionTemplate> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setWidth(25, Unit.EM);
		return comboBox;
	}
	
	private TextField initTextField() {
		TextField field = new TextField();
		field.setWidth(10, Unit.EM);
		return field;
	}
	
	private ComboBox<Priority> initPriorityField() {
		ComboBox<Priority> priorityField = new ComboBox<Priority>();
		priorityField.setTextInputAllowed(false);
		priorityField.setEmptySelectionAllowed(false);
		priorityField.setWidth(10, Unit.EM);
		priorityField.setItems(Priority.values());
		return priorityField;
	}
	
	private ComboBox<Status> initStatusField() {
		ComboBox<Status> statusField = new ComboBox<Status>();
		statusField.setWidth(10, Unit.EM);
		return statusField;
	}
	
	private ComboBox<Integer> initProgressPercentageField() {
		ComboBox<Integer> progressPercentageField = new ComboBox<Integer>();
		progressPercentageField.setWidth(10, Unit.EM);
		progressPercentageField.setEmptySelectionAllowed(false);
		progressPercentageField.setWidth(10, Unit.EM);
		progressPercentageField.setItems(Arrays.asList(0, 10, 25, 50, 75, 100));
		return progressPercentageField;
	}
	
	private ComboBox<User> initTechnicianField() {
		ComboBox<User> comboBox = new ComboBox<>();
		comboBox.setWidth(10, Unit.EM);
		return comboBox;
	}
	
	
	private DateTimeField initDeadlineDateField() {
		DateTimeField dateField = new DateTimeField();
		dateField.setWidth(10, Unit.EM);
		return dateField;
	}
	
	private TextArea initDescriptionField() {
		TextArea textArea = new TextArea();
		textArea.setWidth(40, Unit.EM);
		textArea.setRows(4);
		return textArea;
	}
}
