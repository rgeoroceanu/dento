package com.company.dento.ui;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.company.dento.model.type.Role;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.page.CalendarPage;
import com.company.dento.ui.page.ErrorPage;
import com.company.dento.ui.page.ExecutionPage;
import com.company.dento.ui.page.ExecutionsPage;
import com.company.dento.ui.page.ProcedurePage;
import com.company.dento.ui.page.ProceduresPage;
import com.company.dento.ui.page.SamplePage;
import com.company.dento.ui.page.SamplesPage;
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
@SpringUI(path = "/cms")
@Theme("dento")
@Widgetset("com.company.dento.ui.widgetset.DentoUIWidgetSet")
public class DentoUI extends UI implements Localizable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * View identifier of the start page.
	 */
	public static final String START_PAGE_NAV_NAME = "start";
	public static final String CALENDAR_PAGE_NAV_NAME = "calendar";
	public static final String PROCEDURES_PAGE_NAV_NAME = "procedures";
	public static final String PROCEDURE_PAGE_NAV_NAME = "procedure";
	public static final String SAMPLES_PAGE_NAV_NAME = "samples";
	public static final String SAMPLE_PAGE_NAV_NAME = "sample";
	public static final String EXECUTIONS_PAGE_NAV_NAME = "executions";
	public static final String EXECUTION_PAGE_NAV_NAME = "execution";
	
	/**
	 * View identifier of the error page.
	 */
	public static final String ERROR_PAGE_NAV_NAME = "error";
	public static final String LOGOUT_URL = "../logout";
	@Autowired
	private StartPage startPage;
	@Autowired
	private CalendarPage calendarPage;
	@Autowired
	private ProceduresPage proceduresPage;
	@Autowired
	private ExecutionsPage executionsPage;
	@Autowired
	private SamplesPage samplesPage;
	@Autowired
	private ProcedurePage procedurePage;
	@Autowired
	private ExecutionPage executionPage;
	@Autowired
	private SamplePage samplePage;
	@Autowired
	private ErrorPage errorPage;
	private Navigator navigator;
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
		navigator = new Navigator(this, this);
		navigator.addView(START_PAGE_NAV_NAME, startPage);
		navigator.addView(CALENDAR_PAGE_NAV_NAME, calendarPage);
		navigator.addView(PROCEDURES_PAGE_NAV_NAME, proceduresPage);
		navigator.addView(PROCEDURE_PAGE_NAV_NAME, procedurePage);
		navigator.addView(SAMPLES_PAGE_NAV_NAME, samplesPage);
		navigator.addView(SAMPLE_PAGE_NAV_NAME, samplePage);
		navigator.addView(EXECUTIONS_PAGE_NAV_NAME, executionsPage);
		navigator.addView(EXECUTION_PAGE_NAV_NAME, executionPage);
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
		localizeRecursive(proceduresPage);
		localizeRecursive(samplesPage);
		localizeRecursive(executionsPage);
		localizeRecursive(procedurePage);
		localizeRecursive(executionPage);
		localizeRecursive(samplePage);
		localizeRecursive(calendarPage);
	}
	
	/**
	 * Navigate to the start page.
	 */
	public void navigateToStartPage() {
		navigator.navigateTo(START_PAGE_NAV_NAME);
	}
	
	/**
	 * Navigate to the calendar page.
	 */
	public void navigateToCalendarPage() {
		navigator.navigateTo(CALENDAR_PAGE_NAV_NAME);
	}
	
	public void navigateToProceduresPage() {
		navigator.navigateTo(PROCEDURES_PAGE_NAV_NAME);
	}
	
	public void navigateToSamplesPage() {
		navigator.navigateTo(SAMPLES_PAGE_NAV_NAME);
	}
	
	public void navigateToSamplePage(Long sampleId) {
		final StringBuilder path = new StringBuilder(SAMPLE_PAGE_NAV_NAME);
		if (sampleId != null) {
			path.append("/");
			path.append(String.valueOf(sampleId));
		}
		navigator.navigateTo(path.toString());
	}
	
	public void navigateToExecutionsPage() {
		navigator.navigateTo(EXECUTIONS_PAGE_NAV_NAME);
	}
	
	public void navigateToExecutionPage(Long executionId) {
		final StringBuilder path = new StringBuilder(EXECUTION_PAGE_NAV_NAME);
		if (executionId != null) {
			path.append("/");
			path.append(String.valueOf(executionId));
		}
		navigator.navigateTo(path.toString());
	}
	
	public void navigateToProcedurePage(Long procedureId) {
		final StringBuilder path = new StringBuilder(PROCEDURE_PAGE_NAV_NAME);
		if (procedureId != null) {
			path.append("/");
			path.append(String.valueOf(procedureId));
		}
		navigator.navigateTo(path.toString());
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
		return getAuthorities().contains(Role.USER.toString()) || getAuthorities().contains(Role.TECHNICIAN.toString());
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