package com.company.dento.ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.dento.model.business.Sample;
import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.ui.Button.ClickListener;

public class SamplesLayout extends PageLayout implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final GridLayout<Sample> gridLayout;
	private static final Map<String, ClickListener> navigationItems;
    static
    {
    	navigationItems = new HashMap<String, ClickListener>();
    	navigationItems.put("samples", e -> DentoUI.getCurrent().navigateToSamplesPage());
    }
	
	public SamplesLayout() {
		gridLayout = new GridLayout<Sample>(DentoUI.SAMPLES_PAGE_NAV_NAME, Sample.class);
		gridLayout.setVisibleColumns("id", "template", "procedure", "created");
		gridLayout.addItemClickListener(e -> DentoUI.getCurrent().navigateToSamplePage(e.getItem().getId()));
		gridLayout.addAddButtonListener(e -> DentoUI.getCurrent().navigateToSamplePage(null));
		super.setNavigationItems(navigationItems);
		this.setContentWidth(70, Unit.PERCENTAGE);
		this.setContent(gridLayout);
	}
	
	public void setItems(List<Sample> items) {
		gridLayout.setItems(items);
	}
	
	@Override
	public void localize() {
		super.localize();
		gridLayout.localize();
	}
	
}
