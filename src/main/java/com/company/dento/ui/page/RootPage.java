package com.company.dento.ui.page;

import com.company.dento.service.DataService;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Layout of the start page containing individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@Route(value = "")
public class RootPage extends Page {

	private static final long serialVersionUID = 1L;

	public RootPage(final DataService dataService) {
		super("Root", dataService);
	}

	@Override
	public void beforeEnter(final BeforeEnterEvent event) {
		event.rerouteTo("start");
	}
}