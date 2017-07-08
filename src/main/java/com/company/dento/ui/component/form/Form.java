package com.company.dento.ui.component.form;

import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Basic form layout with save, discard and remove buttons.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class Form extends VerticalLayout implements Localizable {

	private static final long serialVersionUID = 1L;
	private final Button saveButton;
	private final Button discardButton;
	private final Button removeButton;
	
	public Form() {
		saveButton = new Button();
		saveButton.addStyleName(ValoTheme.BUTTON_LINK);
		discardButton = new Button();
		discardButton.addStyleName(ValoTheme.BUTTON_LINK);
		removeButton = new Button();
		removeButton.addStyleName(ValoTheme.BUTTON_LINK);
		
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(discardButton);
		buttonLayout.addComponent(removeButton);
		
		this.addComponent(buttonLayout);
	}
	
	public void addSaveButtonListener(final ClickListener listener) {
		saveButton.addClickListener(listener);
	}
	
	public void addRemoveButtonListener(final ClickListener listener) {
		removeButton.addClickListener(listener);
	}
	
	public void addDiscardButtonListener(final ClickListener listener) {
		discardButton.addClickListener(listener);
	}
	
	public void addActionButton(final Button button) {
		button.addStyleName(ValoTheme.BUTTON_LINK);
		final HorizontalLayout buttonsLayout = (HorizontalLayout) this.getComponent(0);
		buttonsLayout.addComponent(button);
	}
	
	public void setActionButtonsEnableState(boolean saveButtonEnabled,
			boolean discardButtonEnabled, boolean removeButtonEnabled) {
		
		saveButton.setVisible(saveButtonEnabled);
		discardButton.setVisible(discardButtonEnabled);
		removeButton.setVisible(removeButtonEnabled);
	}

	@Override
	public void localize() {
		saveButton.setCaption(Localizer.getLocalizedString("save"));
		discardButton.setCaption(Localizer.getLocalizedString("discard"));
		removeButton.setCaption(Localizer.getLocalizedString("remove"));
	}
}
