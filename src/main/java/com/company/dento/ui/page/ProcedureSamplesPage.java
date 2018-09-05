package com.company.dento.ui.page;

import com.company.dento.model.business.Sample;
import com.company.dento.ui.localization.Localizable;

public class ProcedureSamplesPage extends GridLayout<Sample> implements Localizable {
	
	private static final long serialVersionUID = 1L;
   
	public ProcedureSamplesPage() {
		super(Sample.class);
		super.setVisibleColumns("id", "template", "procedure","created");
		this.setSpacing(true);
		this.setMargin(true);
	}
	
	@Override
	public void localize() {
		super.localize();
	}
}
