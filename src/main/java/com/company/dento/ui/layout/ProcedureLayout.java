package com.company.dento.ui.layout;

import com.company.dento.ui.component.common.NavigationMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class ProcedureLayout extends PageLayout {
	
	private static final long serialVersionUID = 1L;
	private final ProcedureOverviewLayout procedureOverviewLayout;
	private final NavigationMenu navigationMenu;
	private final HorizontalLayout layout;
	
	public ProcedureLayout() {
		procedureOverviewLayout = new ProcedureOverviewLayout();
		navigationMenu = new NavigationMenu();
		navigationMenu.addNavigationButton("overview", VaadinIcons.GLOBE, e -> {int i = 0;});
		navigationMenu.addNavigationButton("teeth", VaadinIcons.TOOTH, e -> {int i = 0;});
		navigationMenu.addNavigationButton("samples", VaadinIcons.WRENCH, e -> {int i = 0;});
		navigationMenu.addNavigationButton("executions", VaadinIcons.TOOLS, e -> {int i = 0;});
		navigationMenu.addNavigationButton("print", VaadinIcons.PRINT, e -> {int i = 0;});
		layout = new HorizontalLayout();
		
		setupLayout();
	}
	
	public void addSaveButtonListener(ClickListener listener) {
		procedureOverviewLayout.addSaveButtonListener(listener);
	}
	
	public void addRemoveButtonListener(ClickListener listener) {
		procedureOverviewLayout.addRemoveButtonListener(listener);
	}
	
	public ProcedureOverviewLayout getProcedureOverviewLayout() {
		return procedureOverviewLayout;
	}
	
	private void setupLayout() {
		layout.addComponent(navigationMenu);
		layout.addComponent(procedureOverviewLayout);
		layout.setSpacing(true);
		this.setContentWidth(70, Unit.PERCENTAGE);
		this.setContent(layout);
	}
	
}
