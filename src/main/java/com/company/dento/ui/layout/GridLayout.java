package com.company.dento.ui.layout;

import java.util.List;

import com.company.dento.model.business.Base;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;


public class GridLayout<T extends Base> extends VerticalLayout implements Localizable {

	private static final long serialVersionUID = 1L;
	private final Grid<T> grid;
	private final Button addButton;
	private Class<T> itemClass;
	
	public GridLayout(String titleMessageId, Class<T> itemClass) {
		this.itemClass = itemClass;
		this.grid = initGrid();
		this.addButton = initAddButton();
		super.addComponent(addButton);
		super.addComponent(grid);
		super.setSizeFull();
		super.setMargin(false);
	}
	
	public void addAddButtonListener(ClickListener listener) {
		addButton.addClickListener(listener);
	}
	
	public void setItems(List<T> items) {
		grid.setItems(items);
	}
	
	public void setVisibleColumns(String... columns) {
		grid.setColumns(columns);
	}
	
	public void addItemClickListener(final ItemClickListener<T> itemClickListener) {
		grid.addItemClickListener(itemClickListener);
	}
	
	private Grid<T> initGrid() {
		final Grid<T> grid = new Grid<T>(itemClass);
		grid.setSizeFull();
		return grid;
	}
	
	private Button initAddButton() {
		final Button button = new Button();
		button.addStyleName(ValoTheme.BUTTON_PRIMARY);
		return button;
	}
	
	@Override
	public void localize() {
		addButton.setCaption(Localizer.getLocalizedString("add"));
		for (Column<?, ?> column : grid.getColumns()) {
			column.setCaption(Localizer.getLocalizedString(column.getId()));
		}
	}
}
