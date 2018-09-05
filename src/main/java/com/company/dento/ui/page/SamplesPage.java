package com.company.dento.ui.page;

import org.springframework.security.access.annotation.Secured;

import com.company.dento.model.business.Sample;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "samples")
public class SamplesPage extends Page implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final Grid<Sample> grid;
	
	public SamplesPage() {
		grid = initGrid();
		grid.setColumns("id", "template", "procedure", "created");
		this.setContent(grid);
	}
	
	@Override
	public void localize() {
		super.localize();
		grid.getColumns().forEach(column -> 
			column.setHeader(Localizer.getLocalizedString(column.getKey())));
	}
	
	@Override
	public void beforeEnter(final BeforeEnterEvent event) {
		super.beforeEnter(event);
		grid.setItems(dataService.getAll(Sample.class));
	}
	
	private Grid<Sample> initGrid() {
		final Grid<Sample> grid = new Grid<Sample>(Sample.class);
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setSizeFull();
		grid.getElement().setAttribute("theme", "no-border row-stripes");
		return grid;
	}
	
}
