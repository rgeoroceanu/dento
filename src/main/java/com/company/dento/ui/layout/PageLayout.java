package com.company.dento.ui.layout;

import java.util.Arrays;
import java.util.Locale;

import com.company.dento.ui.DentoUI;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Layout of all pages of this application. It is composed mainly by header, content and footer.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class PageLayout extends VerticalLayout implements Localizable {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Dento";
	private static final String TITLE_STYLE = "'line-height:80px;margin-left:15px;margin-top:0px;"
			+ "margin-bottom:0px;background-color:rgba(0,0,0,0);color:white;font-size:35px'";
	private static final Locale ENGLISH_LOCALE = new Locale("en");
	private static final Locale GERMAN_LOCALE = new Locale("de");
	
	private final ComboBox<Locale> languageSelect;
	private final MenuBar menuBar;
	private final Button logoutButton;
	private final MenuItem homeItem;
	private final Panel contentPanel;
	private final Button contactButton;
	private final Button helpButton;
	private final Button aboutButton;
	private Label titleLabel;
	
	public PageLayout() {
		this.addStyleName(ValoTheme.UI_WITH_MENU);
		this.titleLabel = initTitleLabel();
		this.languageSelect = initLanguageSelect();
		this.logoutButton = initLogoutButton();
		this.menuBar = new MenuBar();
		this.homeItem = menuBar.addItem("", null, command -> DentoUI.getCurrent().navigateToStartPage());
		this.homeItem.setStyleName("button");
		this.menuBar.addStyleName("titlemenubar");
		this.menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		this.menuBar.setHeight(100, Unit.PERCENTAGE);
		this.contactButton = initContactButton();
		this.helpButton = initHelpButton();
		this.aboutButton = initAboutButton();
		contentPanel = new Panel();
		
		HorizontalLayout headerLayout = new HorizontalLayout();
		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.addStyleName("title");
		HorizontalLayout titleButtonsLayout = new HorizontalLayout();
		HorizontalLayout footerLayout = new HorizontalLayout();
		headerLayout.addComponent(languageSelect);
		headerLayout.addComponent(logoutButton);
		titleButtonsLayout.addComponent(menuBar);
		titleLayout.addComponent(titleLabel);
		titleLayout.addComponent(titleButtonsLayout);
		footerLayout.addComponent(contactButton);
		footerLayout.addComponent(helpButton);
		footerLayout.addComponent(aboutButton);
		this.addComponent(headerLayout);
		this.addComponent(titleLayout);
		this.addComponent(contentPanel);
		this.addComponent(footerLayout);
		
		// aligning
		headerLayout.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
		headerLayout.setComponentAlignment(languageSelect, Alignment.MIDDLE_LEFT);
		titleLayout.setComponentAlignment(titleButtonsLayout, Alignment.TOP_RIGHT);
		
		// sizing
		headerLayout.setWidth(100, Unit.PERCENTAGE);
		headerLayout.setHeight(20, Unit.PIXELS);
		titleLayout.setWidth(100, Unit.PERCENTAGE);
		titleLayout.setHeight(80, Unit.PIXELS);
		footerLayout.setHeight(70, Unit.PIXELS);
		this.setComponentAlignment(footerLayout, Alignment.BOTTOM_CENTER);
		this.setWidth(100, Unit.PERCENTAGE);
		this.setMargin(false);
	}
	
	@Override
	public void localize() {
		// localize
		logoutButton.setCaption(Localizer.getLocalizedString("logout"));
		contactButton.setCaption(Localizer.getLocalizedString("contact"));
		helpButton.setCaption(Localizer.getLocalizedString("help"));
		aboutButton.setCaption(Localizer.getLocalizedString("about"));
		homeItem.setText(Localizer.getLocalizedString("home"));
	}
	
	/**
	 * Set the component that is displayed in the content section.
	 * @param content component to be displayed.
	 */
	public void setContent(final Component content) {
		contentPanel.setContent(content);
	}
	
	/**
	 * Align content to the center of the page.
	 */
	public void alignCenterContent() {
		this.setComponentAlignment(contentPanel, Alignment.TOP_CENTER);
	}
	
	/**
	 * Set width of the content section.
	 * @param width value
	 * @param unit for the width.
	 */
	public void setContentWidth(int width, Unit unit) {
		contentPanel.setWidth(width, unit);
	}
	
	/**
	 * Removes border from the content section.
	 */
	public void setContentBorderless() {
		contentPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
	}
	
	private Label initTitleLabel() {
		final Label label = new Label();
		label.setContentMode(ContentMode.HTML);
		label.setValue("<p style=" + TITLE_STYLE + ">" + TITLE + "</p>");
		label.setHeight(100, Unit.PERCENTAGE);
		return label;
	}
	
	private ComboBox<Locale> initLanguageSelect() {
		final ComboBox<Locale> languageSelect = new ComboBox<Locale>();
		languageSelect.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
		languageSelect.addStyleName(ValoTheme.COMBOBOX_TINY);
		languageSelect.addStyleName(ValoTheme.LINK_SMALL);
		languageSelect.setItems(Arrays.asList(ENGLISH_LOCALE, GERMAN_LOCALE));
		languageSelect.setItemCaptionGenerator(locale -> {
			if (ENGLISH_LOCALE.equals(locale)) {
				return "English";
			} else if (GERMAN_LOCALE.equals(locale)) {
				return "Deutsch";
			}
			return "";
		});
		languageSelect.setTextInputAllowed(false);
		languageSelect.setEmptySelectionAllowed(false);
		languageSelect.addValueChangeListener(e -> changeLocale(e.getValue()));
		languageSelect.setValue(ENGLISH_LOCALE);
		languageSelect.setWidth(7, Unit.EM);
		return languageSelect;
	}
	
	private Button initLogoutButton() {
		final Button logoutButton = new Button();
		logoutButton.addStyleName(ValoTheme.BUTTON_LINK);
		logoutButton.addClickListener(e -> DentoUI.getCurrent().logout());
		return logoutButton;
	}
	
	private Button initContactButton() {
		Button contactButton = new Button();
		contactButton.addStyleName(ValoTheme.BUTTON_LINK);
		return contactButton;
	}
	
	private Button initHelpButton() {
		Button helpButton = new Button();
		helpButton.addStyleName(ValoTheme.BUTTON_LINK);
		return helpButton;
	}
	
	private Button initAboutButton() {
		Button aboutButton = new Button();
		aboutButton.addStyleName(ValoTheme.BUTTON_LINK);
		return aboutButton;
	}
	
	private void changeLocale(Locale locale) {
		Localizer.setLocale(locale);
		DentoUI app = DentoUI.getCurrent();
		if (app != null) {
			app.localize();
		}
	}
}
