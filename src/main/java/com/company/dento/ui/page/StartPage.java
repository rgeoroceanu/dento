package com.company.dento.ui.page;

import org.springframework.stereotype.Component;

import com.company.dento.ui.layout.StartLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

/**
 * Home page of the application. Displays individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@SpringView
public class StartPage extends Page {
	
	private static final long serialVersionUID = 1L;
	private final StartLayout startLayout;
	
	public StartPage() {
		startLayout = new StartLayout();
		this.setLayout(startLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
	}
}
