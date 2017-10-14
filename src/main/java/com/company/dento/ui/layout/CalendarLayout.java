package com.company.dento.ui.layout;


import org.vaadin.addon.calendar.Calendar;

import com.company.dento.model.business.CalendarItem;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.ui.VerticalLayout;

public class CalendarLayout extends PageLayout implements Localizable {
	
	private static final long serialVersionUID = 1L;
	private VerticalLayout layout;
	private Calendar<CalendarItem> calendar;
	
	public CalendarLayout() {
		layout = new VerticalLayout();
		calendar = initCalendar();
		setupLayout();
	}
	
	
	@Override
	public void localize() {
		super.localize();
	}
	
	private void setupLayout() {
		layout.addComponent(calendar);
		layout.setSpacing(true);
		this.setContentWidth(90, Unit.PERCENTAGE);
		this.setContent(layout);
	}
	
	private Calendar<CalendarItem> initCalendar() {
		final Calendar<CalendarItem> calendar = new Calendar<>();
		return calendar;
	}
}
