package com.company.dento.ui.layout;

import com.company.dento.model.business.Sample;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;

public class ProcedureSamplesLayout extends GridLayout<Sample> implements Localizable {
	
	private static final long serialVersionUID = 1L;
   
	public ProcedureSamplesLayout() {
		super(DentoUI.SAMPLES_PAGE_NAV_NAME, Sample.class);
		super.setVisibleColumns("id", "template", "procedure","created");
		super.addItemClickListener(e -> DentoUI.getCurrent().navigateToSamplePage(e.getItem().getId()));
		this.setSpacing(true);
		this.setMargin(true);
	}
	
	@Override
	public void localize() {
		super.localize();
	}
}
