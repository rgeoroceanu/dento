package com.company.dento.ui.layout;

import com.company.dento.ui.component.common.NavigationMenu;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;

public class ExecutionLayout extends PageLayout implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private final ExecutionOverviewLayout executionOverviewLayout;
	private final NavigationMenu navigationMenu;
	private final HorizontalLayout layout;
	private Layout currentContent = null;
	
	public ExecutionLayout() {
		executionOverviewLayout = new ExecutionOverviewLayout();
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
	
	public ExecutionOverviewLayout getExecutionOverviewLayout() {
		return executionOverviewLayout;
	}
	
	@Override
	public void localize() {
		super.localize();
		executionOverviewLayout.localize();
	}
	
	private void setupLayout() {
		layout.addComponent(navigationMenu);
		setContentLayout(executionOverviewLayout);
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
		navigationMenu.addNavigationButton("overview", VaadinIcons.GLOBE, e -> setContentLayout(executionOverviewLayout));
		navigationMenu.addNavigationButton("materials", VaadinIcons.TOOTH, e -> {});
		return navigationMenu;
	}
}
