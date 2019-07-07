package com.company.dento.ui.page;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

/**
 * Error view of the cms.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@Route("accessDenied")
public class AccessDeniedPage extends VerticalLayout implements BeforeEnterObserver {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_MESSAGE = "Access interzis! Vă rugăm să contactați administratorul!";

	/**
	 *
	 */
	public AccessDeniedPage() {
		final Label label = new Label();
		label.setText(DEFAULT_ERROR_MESSAGE);
		this.add(label);
	}

	@Override
	public void beforeEnter(final BeforeEnterEvent event) {
		// TODO Auto-generated method stub
	}
}
