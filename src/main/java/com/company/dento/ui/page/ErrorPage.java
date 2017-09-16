package com.company.dento.ui.page;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.stereotype.Component;

/**
 * Error view of the cms.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@SpringView
public class ErrorPage extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_MESSAGE = "Oops! An error occured! You may have navigated to an invalid page!";
	private final Label label;
	
	/**
	 * 
	 */
	public ErrorPage() {
		label = new Label();
		label.setValue(DEFAULT_ERROR_MESSAGE);
		this.addComponent(label);
	}

	@Override
	public final void enter(final ViewChangeEvent event) {
		final String message = event.getParameters();
		if (message != null) {
			label.setValue(message);
		}
	}
}
