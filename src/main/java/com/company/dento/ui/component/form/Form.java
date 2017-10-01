package com.company.dento.ui.component.form;

import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Basic form layout with save, discard and remove buttons.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class Form extends FormLayout implements Localizable {

	private static final long serialVersionUID = 1L;
	private final Button saveButton;
	private final Button removeButton;
	private final HorizontalLayout buttonLayout;
	
	public Form() {
		saveButton = new Button();
		saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		removeButton = new Button();
		removeButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(removeButton);
		
		this.addComponent(buttonLayout);
		this.setSpacing(true);
		this.setMargin(false);
	}
	
	@Override
	public void addComponent(Component c) {
		super.addComponent(c);
	}
	
	public void addSaveButtonListener(final ClickListener listener) {
		saveButton.addClickListener(listener);
	}
	
	public void addRemoveButtonListener(final ClickListener listener) {
		removeButton.addClickListener(listener);
	}
	
	public void addActionButton(final Button button) {
		button.addStyleName(ValoTheme.BUTTON_PRIMARY);
		final HorizontalLayout buttonsLayout = (HorizontalLayout) this.getComponent(0);
		buttonsLayout.addComponent(button);
	}
	
	public void setActionButtonsEnableState(boolean saveButtonEnabled, boolean removeButtonEnabled) {
		saveButton.setVisible(saveButtonEnabled);
		removeButton.setVisible(removeButtonEnabled);
		if (!saveButtonEnabled && !removeButtonEnabled) {
			buttonLayout.setVisible(false);
		} else {
			buttonLayout.setVisible(true);
		}
	}

	@Override
	public void localize() {
		saveButton.setCaption(Localizer.getLocalizedString("save"));
		removeButton.setCaption(Localizer.getLocalizedString("remove"));
	}
}
