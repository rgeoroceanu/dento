package com.company.dento.ui.page;

import com.company.dento.model.business.CalendarEvent;
import com.company.dento.model.type.CalendarEventType;
import com.company.dento.service.DataService;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.edit.OrderEditPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Component
@UIScope
@Route(value = "calendar")
public class CalendarPage extends Page implements AfterNavigationObserver {

	private static final long serialVersionUID = 1L;
	private final FullCalendar calendar;
	private final VerticalLayout content = new VerticalLayout();
	private final Button previous = new Button();
	private final Button next = new Button();
	private Label dateLabel = new Label();

	public CalendarPage(final DataService dataService) {
		super("Programări", dataService);
		calendar = FullCalendarBuilder.create().build();
		initLayout();
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
		super.afterNavigation(afterNavigationEvent);
		reload();
	}

	private void reload() {
		calendar.changeView(CalendarViewImpl.MONTH);
		calendar.setLocale(Localizer.getCurrentLocale());
	}

	private void initLayout() {
		final Icon previousIcon = new Icon(VaadinIcon.ARROW_BACKWARD);
		previous.setIcon(previousIcon);
		previous.addClassNames("dento-button-simple", "main-layout__content-menu-button");
		previous.addClickListener(e -> calendar.previous());

		final Icon nextIcon = new Icon(VaadinIcon.ARROW_FORWARD);
		next.setIcon(nextIcon);
		next.addClassNames("dento-button-simple", "main-layout__content-menu-button");
		next.addClickListener(e -> calendar.next());

		calendar.getStyle().set("margin-top", "-40px");
		calendar.addViewRenderedListener(this::updateEntries);
		calendar.addEntryClickedListener(e -> handleEntryClicked(e.getEntry()));

		content.setSizeFull();
		content.getStyle().set("overflow-y", "auto");
		content.setPadding(false);

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.add(previous, dateLabel, next);
		buttonLayout.setAlignItems(Alignment.CENTER);
		content.add(buttonLayout);
		content.add(calendar);
		content.setMargin(false);
		content.setFlexGrow(1, calendar);
		this.setContent(content);
	}

	private void updateEntries(final ViewRenderedEvent event) {
		dateLabel.setText(DateTimeFormatter.ofPattern("MMMM yyyy",
				Localizer.getCurrentLocale()).format(event.getIntervalStart()));

		calendar.removeAllEntries();
		calendar.addEntries(dataService.getCalendarEvents(event.getStart(), event.getEnd()).stream()
				.map(this::createEntry)
				.collect(Collectors.toList()));
	}

	private Entry createEntry(final CalendarEvent event) {
		final Entry entry = new Entry();
		final String text = event.getText() != null ? event.getText() : "";
		entry.setTitle(String.format("%s%s%s", event.getType().getTitle(), System.lineSeparator(), text));
		entry.setDescription(String.format("%s, %s", event.getType().name(),
				event.getOrder() != null ? event.getOrder().getId() : event.getSample() != null ? event.getSample().getJob().getOrder().getId() : ""));

		final LocalDateTime dateTime = event.getDate().atTime(event.getTime() != null ? event.getTime() : LocalTime.of(8, 0));
		entry.setStart(dateTime);
		entry.setColor(getEventColor(event.getType()));
		entry.setEditable(false);

		return entry;
	}

	private String getEventColor(final CalendarEventType type) {
		switch (type) {
			case ORDER_DELIVERY:
				return "red";
			case SAMPLE:
				return "green";
			default:
				return "yellow";
		}
	}

	private void handleEntryClicked(final Entry entry) {
		final String[] description = entry.getDescription().split(",");

		if (description.length < 2) {
			return;
		}

		final String type = description[0];
		final Long id;
		try {
			id = NumberUtils.parseNumber(description[1], Long.class);
		} catch (NumberFormatException e) {
			return;
		}

		switch (type) {
			case "ORDER_DELIVERY":
				UI.getCurrent().navigate(OrderEditPage.class, id);
				break;
			case "SAMPLE":
				UI.getCurrent().navigate(OrderEditPage.class, id);
				break;
			default:
				break;
		}
	}
}