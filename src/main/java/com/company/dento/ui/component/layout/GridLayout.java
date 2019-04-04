package com.company.dento.ui.component.layout;

import java.util.List;

import com.company.dento.model.business.Base;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionListener;


public class GridLayout<T extends Base> extends VerticalLayout implements Localizable {

	private static final long serialVersionUID = 1L;
	private final Grid<T> grid;
	private Class<T> itemClass;

	public GridLayout(final Class<T> itemClass) {
		this.itemClass = itemClass;
		this.grid = initGrid();
		this.add(grid);
		this.setSizeFull();
		this.setMargin(false);
	}

	public void setItems(List<T> items) {
		grid.setItems(items);
	}

	public void setVisibleColumns(String... columns) {
		grid.setColumns(columns);
	}

	public void addSelectionListener(final SelectionListener<Grid<T>, T> selectionListener) {
		grid.addSelectionListener(selectionListener);
	}
	
	private Grid<T> initGrid() {
		final Grid<T> grid = new Grid<T>(itemClass);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setSizeFull();
		return grid;
	}
	
	@Override
	public void localize() {
		grid.getColumns().forEach(column -> 
			column.setHeader(Localizer.getLocalizedString(column.getId().orElse(""))));
	}
}
