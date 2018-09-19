package com.company.dento.ui.page;

import com.company.dento.model.business.Job;
import com.company.dento.ui.localization.Localizable;

public class ProcedureExecutionsPage extends GridLayout<Job> implements Localizable {
	
	private static final long serialVersionUID = 1L;
   
	public ProcedureExecutionsPage() {
		super(Job.class);
		super.setVisibleColumns("id", "template", "technician", "procedure", "status", "created");
		this.setSpacing(true);
		this.setMargin(true);
	}
	
	@Override
	public void localize() {
		super.localize();
	}
}
