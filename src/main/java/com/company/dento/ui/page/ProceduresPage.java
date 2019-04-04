package com.company.dento.ui.page;

import java.util.List;

import com.company.dento.model.business.Order;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.layout.GridLayout;
import com.company.dento.ui.localization.Localizable;

public class ProceduresPage extends Page implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final GridLayout<Order> gridLayout;
	
	public ProceduresPage(final DataService dataService) {
	    super(dataService);
		gridLayout = new GridLayout<Order>(Order.class);
		gridLayout.setVisibleColumns("id", "doctor", "patient", "created");
		this.setContent(gridLayout);
	}
	
	public void setItems(List<Order> items) {
		gridLayout.setItems(items);
	}
	
	@Override
	public void localize() {
		super.localize();
		gridLayout.localize();
	}
	
}
