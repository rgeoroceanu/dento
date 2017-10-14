package com.company.dento.ui.layout;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Doctor;
import com.company.dento.model.business.ProcedureTemplate;
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
public class ProcedureOverviewLayout extends Form implements Localizable {

	private static final long serialVersionUID = 1L;
	
	private final ComboBox<ProcedureTemplate> procedureTypeField;
	private final TextField priceField;
	private final ComboBox<Doctor> doctorField;
	private final TextField patientField;
	private final DateTimeField deliveryDateField;
	private final TextArea descriptionField;
	
	public ProcedureOverviewLayout() {
		procedureTypeField = initProcedureTypeField();
		priceField = initPriceField();
		doctorField = initDoctorField();
		patientField = initPatientField();
		deliveryDateField = initDeliveryDateField();
		descriptionField = initDescriptionField();
		setupLayout();
	}
	
	@Override
	public void localize() {
		super.localize();
		procedureTypeField.setCaption(Localizer.getLocalizedString("type"));
		priceField.setCaption(Localizer.getLocalizedString("price"));
		doctorField.setCaption(Localizer.getLocalizedString("doctor"));
		patientField.setCaption(Localizer.getLocalizedString("patient"));
		deliveryDateField.setCaption(Localizer.getLocalizedString("delivery_date"));
		descriptionField.setCaption(Localizer.getLocalizedString("description"));
	}
	
	private void setupLayout() {
		this.addComponent(procedureTypeField);
		this.addComponent(doctorField);
		this.addComponent(patientField);
		this.addComponent(deliveryDateField);
		this.addComponent(priceField);
		this.addComponent(descriptionField);
		this.setMargin(true);
	}
	
	private ComboBox<ProcedureTemplate> initProcedureTypeField() {
		ComboBox<ProcedureTemplate> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setWidth(25, Unit.EM);
		return comboBox;
	}
	
	private TextField initPriceField() {
		TextField priceField = new TextField();
		priceField.setWidth(10, Unit.EM);
		return priceField;
	}
	
	private ComboBox<Doctor> initDoctorField() {
		ComboBox<Doctor> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setWidth(10, Unit.EM);
		return comboBox;
	}
	
	private TextField initPatientField() {
		TextField patientField = new TextField();
		patientField.setWidth(10, Unit.EM);
		return patientField;
	}
	
	private DateTimeField initDeliveryDateField() {
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
