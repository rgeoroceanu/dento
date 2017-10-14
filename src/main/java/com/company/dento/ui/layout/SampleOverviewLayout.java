package com.company.dento.ui.layout;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Procedure;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.ui.component.form.Form;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.TextArea;

import lombok.Getter;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@Getter
public class SampleOverviewLayout extends Form implements Localizable {

	private static final long serialVersionUID = 1L;
	
	private final ComboBox<Procedure> procedureField;
	private final ComboBox<SampleTemplate> sampleTypeField;
	private final TextArea descriptionField;
	private final DateTimeField dateField;
	
	public SampleOverviewLayout() {
		procedureField = initProcedureField();
		sampleTypeField = initSampleTypeField();
		descriptionField = initDescriptionField();
		dateField = initDateField();
		setupLayout();
	}
	
	@Override
	public void localize() {
		super.localize();
		procedureField.setCaption(Localizer.getLocalizedString("procedure"));
		descriptionField.setCaption(Localizer.getLocalizedString("description"));
		dateField.setCaption(Localizer.getLocalizedString("date"));
		sampleTypeField.setCaption(Localizer.getLocalizedString("type"));
	}
	
	private void setupLayout() {
		this.addComponent(procedureField);
		this.addComponent(sampleTypeField);
		this.addComponent(dateField);
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
	
	private ComboBox<SampleTemplate> initSampleTypeField() {
		ComboBox<SampleTemplate> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setWidth(25, Unit.EM);
		return comboBox;
	}
	
	
	private DateTimeField initDateField() {
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
