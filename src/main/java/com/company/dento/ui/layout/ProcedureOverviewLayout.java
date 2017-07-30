package com.company.dento.ui.layout;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Doctor;
import com.company.dento.model.business.ProcedureTemplate;
import com.company.dento.ui.component.form.Form;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
public class ProcedureOverviewLayout extends Form implements Localizable {

	private static final long serialVersionUID = 1L;
	
	private final ComboBox<ProcedureTemplate> procedureType;
	private final TextField priceField;
	private final ComboBox<Doctor> doctorField;
	private final TextField patientField;
	private final DateField deliveryDateField;
	private final TextArea descriptionField;
	
	public ProcedureOverviewLayout() {
		procedureType = initProcedureTypeField();
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
		procedureType.setCaption("type");
		priceField.setCaption("price");
		doctorField.setCaption("doctor");
		patientField.setCaption("patient");
		deliveryDateField.setCaption("delivery_date");
		descriptionField.setCaption("description");
	}
	
	private void setupLayout() {
		this.addComponent(procedureType);
		this.addComponent(doctorField);
		this.addComponent(patientField);
		this.addComponent(deliveryDateField);
		this.addComponent(priceField);
		this.addComponent(descriptionField);
		this.setMargin(true);
	}
	
	private ComboBox<ProcedureTemplate> initProcedureTypeField() {
		ComboBox<ProcedureTemplate> comboBox = new ComboBox<>();
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
		comboBox.setWidth(10, Unit.EM);
		return comboBox;
	}
	
	private TextField initPatientField() {
		TextField patientField = new TextField();
		patientField.setWidth(10, Unit.EM);
		return patientField;
	}
	
	private DateField initDeliveryDateField() {
		DateField dateField = new DateField();
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
