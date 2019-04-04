package com.company.dento.ui.page;

import com.company.dento.model.type.Role;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.layout.MenuLayout;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Layout of all pages of this application. It is composed mainly by header, content and footer.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@HtmlImport("frontend://styles/shared-styles.html")
@HtmlImport("frontend://styles/language-select.html")
@HtmlImport("frontend://styles/dento-confirm-dialog.html")
@HtmlImport("frontend://styles/dento-grid-date-picker.html")
@HtmlImport("frontend://styles/dento-filterable-grid.html")
@HtmlImport("frontend://styles/dento-noheader-grid.html")
@HtmlImport("frontend://styles/upload-display-button.html")
public abstract class Page extends HorizontalLayout implements Localizable, BeforeEnterObserver, PageConfigurator {
	private static final long serialVersionUID = 1L;
	
	protected DataService dataService;
	
	private static final Locale ENGLISH_LOCALE = new Locale("en");
	private static final Locale ROMANIAN_LOCALE = new Locale("ro");
	
	private final ComboBox<Locale> languageSelect;
	private final Button logoutButton;
	private final Div contentLayout;
	private final MenuLayout menuLayout;
	
	public Page(final DataService dataService) {
		this.dataService = dataService;
		this.languageSelect = initLanguageSelect();
		this.logoutButton = initLogoutButton();
		this.contentLayout = new Div();
		this.menuLayout = new MenuLayout();
		initLayout();
		Localizer.setLocale(ROMANIAN_LOCALE);
        contentLayout.addClassName("dento-page");
	}

	@Override
	public void configurePage(InitialPageSettings initialPageSettings) {
		final LoadingIndicatorConfiguration conf = initialPageSettings.getLoadingIndicatorConfiguration();
		conf.setApplyDefaultTheme(false);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		localizeRecursive(this);
	}
	
	/**
	 * Check if current has simple authority.
	 * @return true if user has USER role.
	 */
	protected boolean isSimpleUser() {
		return getAuthorities().contains(Role.USER.toString()) || getAuthorities().contains(Role.TECHNICIAN.toString());
	}
	
	/**
	 * Check if current user has ADMIN authority.
	 * @return true if user has admin role.
	 */
	protected boolean isAdminUser() {
		return getAuthorities().contains(Role.ADMIN.toString());
	}
	
	private Set<String> getAuthorities() {
		Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		Set<String> authorities = new HashSet<>();
		if(auth!= null) {
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
			if(user != null && user.getAuthorities() != null) {
				for(GrantedAuthority ga : user.getAuthorities()) {
					authorities.add(ga.getAuthority());
				}
			}
		}
		return authorities;
	}
	
	private void localizeRecursive(final Component root) {
		if(root instanceof Localizable) {
			((Localizable) root).localize();
		}
		for(Component child : root.getChildren().collect(Collectors.toList())) {
	        if(child instanceof Localizable) {
	        	final Localizable localizable = (Localizable) child;
	        	localizable.localize();
	        	if(child instanceof Component) {
	        		localizeRecursive((Component) child);
	        	}
	        } else if(child instanceof Component) {
	        	localizeRecursive((Component) child);
	        }
	    }
	}
	
	@Override
	public void localize() {
		// localize
	}
	
	/**
	 * Set the component that is displayed in the content section.
	 * @param content component to be displayed.
	 */
	protected void setContent(final Component content) {
		this.contentLayout.add(content);
	}
	
	private void initLayout() {
		final Div headerWrapper = new Div();
		final HorizontalLayout headerButtonsLayout = new HorizontalLayout();
		final VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.getElement().getStyle().set("overflow-y", "hidden");
		headerWrapper.setClassName("main-layout__header");
		headerButtonsLayout.setClassName("main-layout__header-buttons");
		rightLayout.add(headerWrapper, contentLayout);
		headerButtonsLayout.add(languageSelect, logoutButton);
		headerWrapper.add(headerButtonsLayout);
		contentLayout.setClassName("main-layout__content");
		this.add(menuLayout);
		this.add(rightLayout);
		this.setHeight("100%");
		this.setMargin(false);
		this.setSpacing(false);
	}
	
	private ComboBox<Locale> initLanguageSelect() {
		final ComboBox<Locale> languageSelect = new ComboBox<Locale>();
		languageSelect.setItems(Arrays.asList(ENGLISH_LOCALE, ROMANIAN_LOCALE));
		languageSelect.setItemLabelGenerator(locale -> {
			if (ENGLISH_LOCALE.equals(locale)) {
				return "EN";
			} else if (ROMANIAN_LOCALE.equals(locale)) {
				return "RO";
			}
			return "";
		});
		languageSelect.setAllowCustomValue(false);
		languageSelect.setPreventInvalidInput(true);
        languageSelect.addValueChangeListener(e -> {
            final String className = ENGLISH_LOCALE.equals(e.getValue()) ? "language-select-en" : "language-select-ro";
            languageSelect.removeClassNames("language-select-en", "language-select-ro");
            languageSelect.addClassName(className);
        });
		languageSelect.setValue(ROMANIAN_LOCALE);
		languageSelect.addClassName("language-select");
        languageSelect.addValueChangeListener(e -> changeLocale(e.getValue())) ;
		return languageSelect;
	}
	
	private Button initLogoutButton() {
		final Button logoutButton = new Button();
		logoutButton.addClickListener(e -> logoutButton.getUI()
				.ifPresent(ui -> ui.getSession().close()));
		logoutButton.setIcon(new Icon(VaadinIcon.SIGN_OUT));
		logoutButton.setClassName("main-layout__header-button");
		return logoutButton;
	}

	private void changeLocale(final Locale locale) {
		Localizer.setLocale(locale);
		localizeRecursive(this);
	}
}
