package com.company.dento.ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.dento.model.business.Execution;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.ui.Button.ClickListener;

public class ExecutionsLayout extends PageLayout implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final GridLayout<Execution> gridLayout;
	private static final Map<String, ClickListener> navigationItems;
    static
    {
    	navigationItems = new HashMap<String, ClickListener>();
    	navigationItems.put("executions", e -> DentoUI.getCurrent().navigateToExecutionsPage());
    }
	
	public ExecutionsLayout() {
		gridLayout = new GridLayout<Execution>(DentoUI.EXECUTIONS_PAGE_NAV_NAME, Execution.class);
		gridLayout.setVisibleColumns("id", "template", "technician", "procedure", "status", "created");
		gridLayout.addItemClickListener(e -> DentoUI.getCurrent().navigateToExecutionPage(e.getItem().getId()));
		gridLayout.addAddButtonListener(e -> DentoUI.getCurrent().navigateToExecutionPage(null));
		super.setNavigationItems(navigationItems);
		this.setContentWidth(70, Unit.PERCENTAGE);
		this.setContent(gridLayout);
	}
	
	public void setItems(List<Execution> items) {
		gridLayout.setItems(items);
	}
	
	@Override
	public void localize() {
		super.localize();
		gridLayout.localize();
	}
	
}
