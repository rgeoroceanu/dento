package com.company.dento.ui.page;

import com.company.dento.dao.specification.MaterialSpecification;
import com.company.dento.dao.specification.OrderSpecification;
import com.company.dento.model.business.CalendarEvent;
import com.company.dento.model.business.Material;
import com.company.dento.model.business.Order;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.Widget;
import com.company.dento.ui.localization.Localizer;
import com.company.dento.ui.page.list.MaterialsPage;
import com.company.dento.ui.page.list.OrdersPage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Layout of the start page containing individual boxes of information.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@org.springframework.stereotype.Component
@UIScope
@Route(value = "start")
public class StartPage extends Page {

	private static final long serialVersionUID = 1L;
	private final Grid<Order> ordersGrid = new Grid<>(Order.class);
	private final Grid<Material> materialsGrid = new Grid<>(Material.class);
	private final FullCalendar calendar = FullCalendarBuilder.create().build();

	public StartPage(final DataService dataService) {
		super("Acasă", dataService);

		final FormLayout formLayout = new FormLayout();
		final VerticalLayout content = new VerticalLayout();
		formLayout.addFormItem(createLastOrdersWidget(), "");
		formLayout.addFormItem(createCalendarWidget(), "");
		formLayout.addFormItem(createMaterialsWidget(), "");
		content.add(formLayout);
		this.setContent(content);
		content.setSizeFull();
		formLayout.setWidthFull();
		content.getStyle().set("overflow-y", "auto");
		final FormLayout.ResponsiveStep rs1 = new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP);
		final FormLayout.ResponsiveStep rs2 = new FormLayout.ResponsiveStep("850px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP);
		final FormLayout.ResponsiveStep rs3 = new FormLayout.ResponsiveStep("1300px", 3, FormLayout.ResponsiveStep.LabelsPosition.TOP);
		final FormLayout.ResponsiveStep rs4 = new FormLayout.ResponsiveStep("1700px", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP);
		formLayout.setResponsiveSteps(rs1, rs2, rs3, rs4);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
		super.afterNavigation(afterNavigationEvent);
		reload();
	}

	@Override
	public void localize() {
		super.localize();
		ordersGrid.getColumns().get(0).setHeader("Doctor");
		ordersGrid.getColumns().get(1).setHeader("Lucrări");
		materialsGrid.getColumns().get(0).setHeader("Material");
		materialsGrid.getColumns().get(1).setHeader("Cantitate");
	}

	private void reload() {
		ordersGrid.setItems(getLastOrders());
		materialsGrid.setItems(getLastMaterials());
		calendar.changeView(CalendarViewImpl.LIST_DAY);
		calendar.setLocale(Localizer.getCurrentLocale());
	}

	private Widget createLastOrdersWidget() {
		final Widget ordersWidget = new Widget("Ultimele comenzi");
		ordersWidget.addClickListener(e -> UI.getCurrent().navigate(OrdersPage.class));
		ordersWidget.setContent(ordersGrid);
		ordersGrid.removeAllColumns();
		ordersGrid.addColumn("doctor").setFlexGrow(0).setWidth("130px");
		ordersGrid.addComponentColumn(item -> createCollectionColumn(item.getJobs().stream()
				.map(job -> job.getTemplate().getName())
				.collect(Collectors.toList())))
				.setKey("job.template.name");

		ordersGrid.getElement().setAttribute("theme", "no-border row-stripes");
		ordersGrid.addClassName("dento-grid");
		return ordersWidget;
	}

	private Widget createCalendarWidget() {
		final Widget calendarWidget = new Widget("Programări");
		calendarWidget.addClickListener(e -> UI.getCurrent().navigate(CalendarPage.class));
		calendar.changeView(CalendarViewImpl.LIST_DAY);
		calendar.setLocale(Localizer.getCurrentLocale());
		calendar.addViewRenderedListener(e -> updateCalendarEntries());
		calendarWidget.setContent(calendar);
		calendar.setSizeFull();
		calendar.setBusinessHours(new BusinessHours(LocalTime.of(8, 0), LocalTime.of(20, 0),BusinessHours.DEFAULT_BUSINESS_WEEK));
		return calendarWidget;
	}

	private Widget createMaterialsWidget() {
		final Widget materialsWidget = new Widget("Materiale");
		materialsWidget.addClickListener(e -> UI.getCurrent().navigate(MaterialsPage.class));
		materialsWidget.setContent(materialsGrid);
		materialsGrid.removeAllColumns();
		materialsGrid.addColumn("template.name");
		materialsGrid.addColumn("quantity").setFlexGrow(0).setWidth("120px");
		materialsGrid.setSizeFull();
		materialsGrid.getElement().setAttribute("theme", "no-border row-stripes");
		materialsGrid.addClassName("dento-grid");
		return materialsWidget;
	}

	private List<Order> getLastOrders() {
		final OrderSpecification orderSpecification = new OrderSpecification();
		return dataService.getByCriteria(Order.class, orderSpecification, 0, 10, Collections.emptyMap());
	}

	private List<Material> getLastMaterials() {
		final MaterialSpecification materialSpecification = new MaterialSpecification();
		return dataService.getByCriteria(Material.class, materialSpecification, 0, 10, Collections.emptyMap());
	}

	private void updateCalendarEntries() {
		calendar.removeAllEntries();
		calendar.addEntries(dataService.getCalendarEvents(LocalDate.now(), LocalDate.now()).stream()
				.map(this::createEntry)
				.collect(Collectors.toList()));
	}

	private Entry createEntry(final CalendarEvent event) {
		final Entry entry = new Entry();
		final String text = event.getText() != null ? event.getText() : "";
		entry.setTitle(String.format("%s%s%s", event.getType().getTitle(), System.lineSeparator(), text));

		final LocalDateTime dateTime = event.getDate().atTime(event.getTime() != null ? event.getTime() : LocalTime.of(8, 0));
		entry.setStart(dateTime);
		entry.setEditable(false);

		return entry;
	}

	private Component createCollectionColumn(final Collection<String> values) {
		final Div div = new Div();
		values.stream().map(Paragraph::new).forEach(div::add);
		return div;
	}
}