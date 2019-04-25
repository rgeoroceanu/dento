package com.company.dento.ui.page;

import com.company.dento.service.DataService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * Layout of the start page containing individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "")
public class RootPage extends Page{

	private static final long serialVersionUID = 1L;

	public RootPage(final DataService dataService) {
		super(dataService);
	}

	@Override
	public void beforeEnter(final BeforeEnterEvent event) {
		event.rerouteTo("start");
	}
}