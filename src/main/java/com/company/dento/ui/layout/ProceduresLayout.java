package com.company.dento.ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.dento.model.business.Procedure;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.ui.Button.ClickListener;

public class ProceduresLayout extends PageLayout implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final GridLayout<Procedure> gridLayout;
	private static final Map<String, ClickListener> navigationItems;
    static
    {
    	navigationItems = new HashMap<String, ClickListener>();
    	navigationItems.put("procedures", e -> DentoUI.getCurrent().navigateToProceduresPage());
    }
	
	public ProceduresLayout() {
		gridLayout = new GridLayout<Procedure>(DentoUI.PROCEDURES_PAGE_NAV_NAME, Procedure.class);
		gridLayout.setVisibleColumns("id", "doctor", "patient", "created");
		gridLayout.addItemClickListener(e -> DentoUI.getCurrent().navigateToProcedurePage(e.getItem().getId()));
		super.setNavigationItems(navigationItems);
		this.setContentWidth(70, Unit.PERCENTAGE);
		this.setContent(gridLayout);
	}
	
	public void setItems(List<Procedure> items) {
		gridLayout.setItems(items);
	}
	
	@Override
	public void localize() {
		super.localize();
		gridLayout.localize();
	}
	
}
