package com.company.dento.ui.layout;

import com.vaadin.ui.GridLayout;

/**
 * Layout of the start page containing individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class StartLayout extends PageLayout {
	
	private static final long serialVersionUID = 1L;
	
	public StartLayout() {
		final GridLayout layout = new GridLayout(3, 2);
		layout.setSpacing(true);
		layout.setMargin(true);
		this.setContent(layout);
	}
}
