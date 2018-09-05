package com.company.dento.ui.page;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Error view of the cms.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
public class ErrorPage extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_MESSAGE = "Oops! An error occured! You may have navigated to an invalid page!";
	private final Label label;
	
	/**
	 * 
	 */
	public ErrorPage() {
		label = new Label();
		label.setText(DEFAULT_ERROR_MESSAGE);
		this.add(label);
	}

	@Override
	public void beforeEnter(final BeforeEnterEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		label.setText(parameter);
	}
}
