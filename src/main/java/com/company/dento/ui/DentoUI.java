package com.company.dento.ui;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.company.dento.model.type.Role;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.page.ErrorPage;
import com.company.dento.ui.page.StartPage;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * UI entry point of the application.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@SpringUI(path = "/")
@Theme("dento")
@Widgetset("com.company.dento.ui.widgetset.DentoUIWidgetSet")
public class DentoUI extends UI implements Localizable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * View identifier of the start page.
	 */
	private static final String START_PAGE_NAV_NAME = "start";
	
	/**
	 * View identifier of the error page.
	 */
	private static final String ERROR_PAGE_NAV_NAME = "error";
	private static final String LOGOUT_URL = "../logout";
	@Autowired
	private StartPage startPage;
	@Autowired
	private ErrorPage errorPage;
	private Navigator navigator;
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
		navigator = new Navigator(this, this);
		navigator.addView(START_PAGE_NAV_NAME, startPage);
		navigator.addView(ERROR_PAGE_NAV_NAME, errorPage);
		navigator.navigateTo(START_PAGE_NAV_NAME);
		navigator.setErrorView(errorPage);
        localize();
    }
	
	/**
	 * Get the current App UI instance.
	 * @return the app UI instance.
	 */
	public static DentoUI getCurrent() {
		return (DentoUI) UI.getCurrent();
	}
	
	@Override
	public void localize() {
		localizeRecursive(startPage);
	}
	
	/**
	 * Navigate to the start searching page.
	 */
	public void navigateToStartPage() {
		navigator.navigateTo(START_PAGE_NAV_NAME);
	}
	
	/**
	 * Navigate to error page, optionally changing the error message.
	 * @param message
	 */
	public void navigateToErrorPage(final String message) {
		navigator.navigateTo(ERROR_PAGE_NAV_NAME + "/" + message);
	}
	
	/**
	 * Logout from application.
	 */
	public void logout() {
		Page.getCurrent().open(LOGOUT_URL, null);
	}
	
	private void localizeRecursive(HasComponents root) {
		if(root instanceof Localizable) {
			((Localizable) root).localize();
		}
		for(Component child : root) {
	        if(child instanceof Localizable) {
	        	final Localizable localizable = (Localizable) child;
	        	localizable.localize();
	        	if(child instanceof HasComponents) {
	        		localizeRecursive((HasComponents) child);
	        	}
	        } else if(child instanceof HasComponents) {
	        	localizeRecursive((HasComponents) child);
	        }
	    }
	}
	
	/**
	 * Check if current has simple authority.
	 * @return true if user has USER role.
	 */
	public boolean isSimpleUser() {
		return getAuthorities().contains(Role.USER.toString());
	}
	
	/**
	 * Check if current user has ADMIN authority.
	 * @return true if user has admin role.
	 */
	public boolean isAdminUser() {
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
}