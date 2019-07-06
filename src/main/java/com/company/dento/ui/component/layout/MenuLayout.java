package com.company.dento.ui.component.layout;

import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.CalendarPage;
import com.company.dento.ui.page.Page;
import com.company.dento.ui.page.StartPage;
import com.company.dento.ui.page.edit.GeneralDataEditPage;
import com.company.dento.ui.page.list.*;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.*;
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
	private final RouterLink calendarButton;
	private final RouterLink doctorsButton;
	private final RouterLink materialsButton;
	private final RouterLink jobTemplatesButton;
	private final RouterLink sampleTemplatesButton;
	private final RouterLink executionTemplatesButton;
	private final RouterLink clinicsButton;
	private final RouterLink usersButton;
	private final RouterLink generalDataButton;
	private final Label titleLabel;
	private final UnorderedList generalButtonsLayout;
	private final UnorderedList adminButtonsLayout;
	private final Span adminLabel;

	public MenuLayout() {
		this.titleLabel = initTitleLabel();
		generalButtonsLayout = new UnorderedList();
		adminButtonsLayout = new UnorderedList();
		adminLabel = new Span();
		init();
		this.homeButton = addMenuItem(StartPage.class, VaadinIcon.HOME);
		this.calendarButton = addMenuItem(CalendarPage.class, VaadinIcon.CALENDAR);
		this.ordersButton = addMenuItem(OrdersPage.class, VaadinIcon.FILE);
		this.executionsButton = addMenuItem(ExecutionsPage.class, VaadinIcon.SPECIALIST);
		initAdminMenu();
		this.generalDataButton = addAdminItem(GeneralDataEditPage.class);
		this.clinicsButton = addAdminItem(ClinicsPage.class);
		this.doctorsButton = addAdminItem(DoctorsPage.class);
		this.executionTemplatesButton = addAdminItem(StartPage.class);
		this.jobTemplatesButton = addAdminItem(JobTemplatesPage.class);
		this.materialsButton = addAdminItem(MaterialsPage.class);
		this.sampleTemplatesButton = addAdminItem(SampleTemplatesPage.class);
		this.usersButton = addAdminItem(UsersPage.class);
	}

	@Override
	public void localize() {
		// localize
		localizeMenuItem(homeButton, "home");
		localizeMenuItem(executionsButton, "executions");
		localizeMenuItem(ordersButton, "orders");
		localizeMenuItem(calendarButton, "calendar");
		adminLabel.setText(Localizer.getLocalizedString("administration"));
		localizeMenuItem(doctorsButton, "doctors");
		localizeMenuItem(materialsButton, "materials");
		localizeMenuItem(executionTemplatesButton, "executions");
		localizeMenuItem(clinicsButton, "clinics");
		localizeMenuItem(sampleTemplatesButton, "samples");
		localizeMenuItem(jobTemplatesButton, "jobs");
		localizeMenuItem(usersButton, "users");
		localizeMenuItem(generalDataButton, "generalData");
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

		this.setClassName("app-sidebar");
		sidebarLogo.setClassName("logo");
		sidebarTitle.setClassName("logo-text");
		sidebarImage.setClassName("logo-img");
		sidebarBackground.setClassName("sidebar-background");
		sidebarContent.setClassName("sidebar-content");
		navContainer.setClassName("nav-container");
		generalButtonsLayout.addClassNames("navigation", "navigation-main");

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

	private <T extends Page> RouterLink addMenuItem(final Class<T> routerClass, final VaadinIcon vaadinIcon) {
		final ListItem listItem = new ListItem();
		listItem.setClassName("nav-item");
		final RouterLink menuItem = new RouterLink(null, routerClass);
		final Icon icon = new Icon(vaadinIcon);
		final Span span = new Span("");
		span.setClassName("menu-title");
		menuItem.add(icon, span);
		listItem.add(menuItem);
		generalButtonsLayout.add(listItem);
		return menuItem;
	}

	private <T extends Page> RouterLink addAdminItem(final Class<T> routerClass) {
		final ListItem listItem = new ListItem();
		listItem.setClassName("nav-item");
		final RouterLink menuItem = new RouterLink(null, routerClass);
		final Span span = new Span("");
		span.setClassName("menu-title");
		menuItem.add(span);
		listItem.add(menuItem);
		adminButtonsLayout.add(listItem);
		return menuItem;
	}

	private void initAdminMenu() {
		final ListItem adminItem = new ListItem();
		adminItem.setClassName("nav-item");
		final Details adminDetails = new Details();
		final Anchor adminAnchor = new Anchor();
		adminDetails.addThemeVariants(DetailsVariant.REVERSE);
		adminAnchor.add(new Icon(VaadinIcon.TOOLS), adminLabel);
		adminDetails.setSummary(adminAnchor);
		adminDetails.setContent(adminButtonsLayout);
		adminLabel.setClassName("menu-title");
		adminItem.add(adminDetails);
		generalButtonsLayout.add(adminItem);
		adminDetails.getElement().getClassList().set("dento-menu-details", true);
	}
}
