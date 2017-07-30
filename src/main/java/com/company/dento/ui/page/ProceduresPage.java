package com.company.dento.ui.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Procedure;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.layout.GridLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button.ClickListener;

/**
 * Page used for managing users and general dealership information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@SpringView
public class ProceduresPage extends Page {

	private static final long serialVersionUID = 1L;
	private static final Map<String, ClickListener> navigationItems;
	    static
	    {
	    	navigationItems = new HashMap<String, ClickListener>();
	    	navigationItems.put("procedures", e -> DentoUI.getCurrent().navigateToProceduresPage());
	    }
	private final GridLayout<Procedure> proceduresLayout;

	public ProceduresPage() {
		super();
		proceduresLayout = new GridLayout<Procedure>(DentoUI.PROCEDURES_PAGE_NAV_NAME, Procedure.class);
		proceduresLayout.setVisibleColumns("id", "doctor", "patient", "created");
		proceduresLayout.addProcedureClickListener(e -> DentoUI.getCurrent().navigateToProcedurePage(e.getItem().getId()));
		proceduresLayout.setNavigationItems(navigationItems);
		//proceduresLayout.addAddButtonListener(click -> App.getCurrent().navigateToUserPage(null));
		this.setLayout(proceduresLayout);
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
		final List<Procedure> procedures = dataService.getAll(Procedure.class);
		proceduresLayout.setItems(procedures);
	}
}
