package com.company.dento.ui.page;

import com.company.dento.service.DataService;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.router.Route;

/**
 * Layout of the start page containing individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
//@Component
//@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "start")
public class StartPage extends Page{
	
	private static final long serialVersionUID = 1L;
	
	public StartPage(final DataService dataService) {
		super(dataService);
	}
}
