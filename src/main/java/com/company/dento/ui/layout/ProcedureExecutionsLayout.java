package com.company.dento.ui.layout;

import com.company.dento.model.business.Execution;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;

public class ProcedureExecutionsLayout extends GridLayout<Execution> implements Localizable {
	
	private static final long serialVersionUID = 1L;
   
	public ProcedureExecutionsLayout() {
		super(DentoUI.EXECUTIONS_PAGE_NAV_NAME, Execution.class);
		super.setVisibleColumns("id", "template", "technician", "procedure", "status", "created");
		super.addItemClickListener(e -> DentoUI.getCurrent().navigateToExecutionPage(e.getItem().getId()));
		this.setSpacing(true);
		this.setMargin(true);
	}
	
	@Override
	public void localize() {
		super.localize();
	}
}
