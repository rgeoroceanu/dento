package com.company.dento.ui.layout;

import com.company.dento.ui.component.common.NavigationMenu;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;

public class ProcedureLayout extends PageLayout implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final ProcedureOverviewLayout procedureOverviewLayout;
	private final ProcedureSamplesLayout procedureSamplesLayout;
	private final ProcedureExecutionsLayout procedureExecutionsLayout;
	private final NavigationMenu navigationMenu;
	private final HorizontalLayout layout;
	private Layout currentContent = null;
	
	public ProcedureLayout() {
		procedureOverviewLayout = new ProcedureOverviewLayout();
		procedureSamplesLayout = new ProcedureSamplesLayout();
		procedureExecutionsLayout = new ProcedureExecutionsLayout();
		navigationMenu = initNavigationMenu();
		layout = new HorizontalLayout();
		setupLayout();
	}
	
	public void addSaveButtonListener(ClickListener listener) {
		navigationMenu.addNavigationButton("save", VaadinIcons.STORAGE, listener);
	}
	
	public void addRemoveButtonListener(ClickListener listener) {
		navigationMenu.addNavigationButton("remove", VaadinIcons.TRASH, listener);
	}
	
	public ProcedureOverviewLayout getProcedureOverviewLayout() {
		return procedureOverviewLayout;
	}
	
	public ProcedureSamplesLayout getProcedureSamplesLayout() {
		return procedureSamplesLayout;
	}
	
	public ProcedureExecutionsLayout getProcedureExecutionsLayout() {
		return procedureExecutionsLayout;
	}
	
	@Override
	public void localize() {
		super.localize();
		procedureOverviewLayout.localize();
		procedureSamplesLayout.localize();
		procedureExecutionsLayout.localize();
	}
	
	private void setupLayout() {
		layout.addComponent(navigationMenu);
		setContentLayout(procedureOverviewLayout);
		layout.setSpacing(true);
		this.setContentWidth(70, Unit.PERCENTAGE);
		this.setContent(layout);
	}
	
	private void setContentLayout(Layout layout) {
		if (currentContent == null) {
			this.layout.addComponent(layout);
		} else {
			this.layout.replaceComponent(currentContent, layout);
		}
		currentContent = layout;
	}
	
	private NavigationMenu initNavigationMenu() {
		final NavigationMenu navigationMenu = new NavigationMenu();
		navigationMenu.addNavigationButton("overview", VaadinIcons.GLOBE, e -> setContentLayout(procedureOverviewLayout));
		navigationMenu.addNavigationButton("teeth", VaadinIcons.TOOTH, e -> {});
		navigationMenu.addNavigationButton("samples", VaadinIcons.WRENCH, e -> setContentLayout(procedureSamplesLayout));
		navigationMenu.addNavigationButton("executions", VaadinIcons.TOOLS, e -> setContentLayout(procedureExecutionsLayout));
		navigationMenu.addNavigationButton("print", VaadinIcons.PRINT, e -> {});
		return navigationMenu;
	}
}
