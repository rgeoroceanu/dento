package com.company.dento.ui.page;

import org.springframework.security.access.annotation.Secured;

import com.company.dento.model.business.Execution;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "executions")
public class ExecutionsPage extends Page implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final Grid<Execution> grid;
	
	public ExecutionsPage() {
		grid = initGrid();
		grid.setColumns("id", "template", "technician", "procedure", "status", "created");
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
		grid.setItems(dataService.getAll(Execution.class));
	}
	
	private Grid<Execution> initGrid() {
		final Grid<Execution> grid = new Grid<Execution>(Execution.class);
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setSizeFull();
		grid.getElement().setAttribute("theme", "no-border row-stripes");
		return grid;
	}
}
