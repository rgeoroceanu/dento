package com.company.dento.ui.page;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Procedure;
import com.company.dento.ui.layout.ProceduresLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

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
	private final ProceduresLayout proceduresLayout;

	public ProceduresPage() {
		super();
		proceduresLayout = new ProceduresLayout();
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
