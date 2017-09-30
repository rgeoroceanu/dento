package com.company.dento.ui.page;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Execution;
import com.company.dento.ui.layout.ExecutionsLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

/**
 * Page that displays a list of all executions.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@SpringView
public class ExecutionsPage extends Page {

	private static final long serialVersionUID = 1L;
	private final ExecutionsLayout executionsLayout;

	public ExecutionsPage() {
		super();
		executionsLayout = new ExecutionsLayout();
		//proceduresLayout.addAddButtonListener(click -> App.getCurrent().navigateToUserPage(null));
		this.setLayout(executionsLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
//		if (DentoUI.getCurrent().isAdmin() == false) {
//			DentoUI.getCurrent().navigateToErrorPage("Permission denied!");
//			return;
//		}
		open();
	}
	
	private void open() {
		final List<Execution> executions = dataService.getAll(Execution.class);
		executionsLayout.setItems(executions);
	}
}
