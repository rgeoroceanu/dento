package com.company.dento.ui.page;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.dento.service.DataService;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.PageLayout;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

/**
 * Abstract class that acts as the presenter of individual view pages. Connects Vaadin layout and components code
 * to the business logic that controls them. Implements {@link View} so that it can be used by {@link Navigator}.
 * All pages that extend this class will have the same display layout.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public abstract class Page extends CustomComponent implements View {
	private static final long serialVersionUID = 1L;
	@Autowired
	protected DataService dataService;
	
	public void setLayout(PageLayout layout) {
		this.setCompositionRoot(layout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		handleAccess();
	}
	
	private void handleAccess() {	
		if (DentoUI.getCurrent().isSimpleUser() == false && DentoUI.getCurrent().isAdminUser() == false) {
			DentoUI.getCurrent().navigateToErrorPage("Permission denied!");
			return;
		}
	}
}