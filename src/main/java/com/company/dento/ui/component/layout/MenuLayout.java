package com.company.dento.ui.component.layout;

import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.ExecutionsPage;
import com.company.dento.ui.page.Page;
import com.company.dento.ui.page.OrdersPage;
import com.company.dento.ui.page.StartPage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class MenuLayout extends Div implements Localizable {
	
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "DENTIVO";

	private final RouterLink homeButton;
	private final RouterLink executionsButton;
	private final RouterLink ordersButton;
	private final Label titleLabel;
	
	public MenuLayout() {
		this.titleLabel = initTitleLabel();
		
		this.homeButton = initMenuItem(StartPage.class, VaadinIcon.HOME);
		this.executionsButton = initMenuItem(ExecutionsPage.class, VaadinIcon.SPECIALIST);
		this.ordersButton = initMenuItem(OrdersPage.class, VaadinIcon.FILE);
		init();
	}
	
	@Override
	public void localize() {
		// localize
		localizeMenuItem(homeButton, "home");
		localizeMenuItem(executionsButton, "executions");
		localizeMenuItem(ordersButton, "orders");
	}
	
	private void localizeMenuItem(final RouterLink routerLink, final String messageId) {
		routerLink.getChildren()
			.filter(c -> c instanceof Span)
			.findFirst()
			.ifPresent(span -> ((Span)span).setText(Localizer.getLocalizedString(messageId)));
	}
	
	private void init() {
        // Add items
		final HorizontalLayout sidebarLogo = new HorizontalLayout();
		final Icon sidebarImage = new Icon(VaadinIcon.TOOTH);
		final Div sidebarTitle = new Div();
		final Div navContainer = new Div();
		final Div sidebarContent = new Div();
		final Div sidebarBackground = new Div();
		final UnorderedList generalButtonsLayout = new UnorderedList();
		final ListItem homeItem = new ListItem();
		final ListItem executionsItem = new ListItem();
		final ListItem ordersItem = new ListItem();
		
		this.setClassName("app-sidebar");
		sidebarLogo.setClassName("logo");
		sidebarTitle.setClassName("logo-text");
		sidebarImage.setClassName("logo-img");
		sidebarBackground.setClassName("sidebar-background");
		sidebarContent.setClassName("sidebar-content");
		navContainer.setClassName("nav-container");
		generalButtonsLayout.setClassName("navigation navigation-main");
		homeItem.setClassName("nav-item");
		executionsItem.setClassName("nav-item");
		ordersItem.setClassName("nav-item");
		
		homeItem.add(homeButton);
		executionsItem.add(executionsButton);
		ordersItem.add(ordersButton);
		generalButtonsLayout.add(homeItem, ordersItem, executionsItem);
		sidebarTitle.add(titleLabel);
		sidebarLogo.add(sidebarImage, sidebarTitle);
		navContainer.add(generalButtonsLayout);
		sidebarContent.add(navContainer);
		this.add(sidebarLogo, sidebarContent, sidebarBackground);
		sidebarLogo.setMargin(false);
		sidebarLogo.setSpacing(false);
    }
	
	private Label initTitleLabel() {
		final Label label = new Label();
		label.setText(TITLE);
		return label;
	}
	
	private <T extends Page> RouterLink initMenuItem(final Class<T> routerClass, final VaadinIcon vaadinIcon) {
		final RouterLink menuItem = new RouterLink(null, routerClass);
		final Icon icon = new Icon(vaadinIcon);
		final Span span = new Span("Acasa");
		span.setClassName("menu-title");
		menuItem.add(icon, span);
		return menuItem;
	}
}
