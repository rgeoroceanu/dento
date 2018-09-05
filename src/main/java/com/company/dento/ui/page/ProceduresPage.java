package com.company.dento.ui.page;

import java.util.List;

import com.company.dento.model.business.Procedure;
import com.company.dento.ui.localization.Localizable;

public class ProceduresPage extends Page implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final GridLayout<Procedure> gridLayout;
	
	public ProceduresPage() {
		gridLayout = new GridLayout<Procedure>(Procedure.class);
		gridLayout.setVisibleColumns("id", "doctor", "patient", "created");
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
