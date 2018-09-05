package com.company.dento.ui;

import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * UI entry point of the application.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
//@Theme(Lumo.class)
public class DentoUI {
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
}