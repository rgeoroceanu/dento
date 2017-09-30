package com.company.dento.ui.page;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.dento.model.business.Sample;
import com.company.dento.ui.layout.SamplesLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

/**
 * Page that displays a list of all samples.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@SpringView
public class SamplesPage extends Page {

	private static final long serialVersionUID = 1L;
	private final SamplesLayout samplesLayout;

	public SamplesPage() {
		super();
		samplesLayout = new SamplesLayout();
		//proceduresLayout.addAddButtonListener(click -> App.getCurrent().navigateToUserPage(null));
		this.setLayout(samplesLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
//		if (DentoUI.getCurrent().isAdmin() == false) {
//			DentoUI.getCurrent().navigateToErrorPage("Permission denied!");
//			return;
//		}
		open();
	}
	
	private void open() {
		final List<Sample> samples = dataService.getAll(Sample.class);
		samplesLayout.setItems(samples);
	}
}
