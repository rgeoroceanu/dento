package com.company.dento.ui.page;

import com.company.dento.model.business.Execution;
import com.company.dento.ui.localization.Localizable;

public class ProcedureExecutionsPage extends GridLayout<Execution> implements Localizable {
	
	private static final long serialVersionUID = 1L;
   
	public ProcedureExecutionsPage() {
		super(Execution.class);
		super.setVisibleColumns("id", "template", "technician", "procedure", "status", "created");
		this.setSpacing(true);
		this.setMargin(true);
	}
	
	@Override
	public void localize() {
		super.localize();
	}
}
