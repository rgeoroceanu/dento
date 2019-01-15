package com.company.dento.ui.page;

import com.company.dento.dao.specification.OrderSpecification;
import com.company.dento.model.business.Clinic;
import com.company.dento.model.business.Doctor;
import com.company.dento.model.business.Order;
import com.company.dento.service.DataService;
import com.company.dento.service.ReportService;
import com.company.dento.service.exception.CannotGenerateReportException;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.access.annotation.Secured;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "orders")
@Log4j2
public class OrdersPage extends ListPage<Order, OrderSpecification> implements Localizable, AfterNavigationObserver {

	private static final long serialVersionUID = 1L;

    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;
    private final ComboBox<Boolean> finalizedFilter;
    private final ComboBox<Boolean> paidFilter;
    private final TextField idFilter;
    private final TextField patientFilter;
    private final ComboBox<Clinic> clinicFilter;
    private final ComboBox<Doctor> doctorFilter;
    private final TextField priceFilter;
    private final ConfirmDialog confirmDialog;
    private final ReportService reportService;

	public OrdersPage(final DataService dataService, final ReportService reportService) {
	    super(Order.class, dataService);
	    this.reportService = reportService;

        clinicFilter = new ComboBox<>();
        doctorFilter = new ComboBox<>();
        fromDateFilter = new DatePicker();
        toDateFilter = new DatePicker();
        finalizedFilter = new ComboBox<>();
        paidFilter = new ComboBox<>();
        idFilter = new TextField();
        patientFilter = new TextField();
        priceFilter = new TextField();
        confirmDialog = new ConfirmDialog();

        grid.addColumn(new LocalDateRenderer<>(Order::getDate, "d.M.yyyy"))
                .setKey("date").setWidth("60px");

        grid.addColumn("id").setWidth("20px");
        grid.addColumn("patient");
        grid.addColumn("clinic.name");
        grid.addColumn("doctor");

        grid.addComponentColumn(this::createFinalizedComponent).setKey("finalized");
        grid.addComponentColumn(this::createPaidComponent).setKey("paid");
        grid.addColumn("price");

        grid.addComponentColumn(item -> createCollectionColumn(item.getJobs().stream()
                .map(job -> job.getTemplate().getName())
                .collect(Collectors.toList())))
                .setKey("job.template.name");

        grid.addComponentColumn(item -> createCollectionColumn(item.getJobs().stream()
                .map(job -> String.valueOf(job.getCount()))
                .collect(Collectors.toList())))
                .setKey("count");

        grid.addComponentColumn(item -> createCollectionColumn(item.getJobs().stream()
                .map(job -> String.valueOf(job.getPrice()))
                .collect(Collectors.toList())))
                .setKey("job.price.element");

        grid.addComponentColumn(item -> createCollectionColumn(item.getJobs().stream()
                .map(job -> String.valueOf(job.getPrice() * job.getCount()))
                .collect(Collectors.toList())))
                .setKey("job.price.total");

        grid.addComponentColumn(this::createPrintComponent).setKey("print").setWidth("15px");

        addEditColumn();
        addRemoveColumn();

        grid.setItemDetailsProviders(Map.of("Pacient", Order::getPatient, "Doctor", Order::getDoctor));

        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));

        initFilters();
        initLayout();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
        finalizedFilter.setItemLabelGenerator(item ->
                item ? Localizer.getLocalizedString("yes") : Localizer.getLocalizedString("no"));
        paidFilter.setItemLabelGenerator(item ->
                item ? Localizer.getLocalizedString("yes") : Localizer.getLocalizedString("no"));
	}

    @Override
    public void afterNavigation(final AfterNavigationEvent event) {
        clearFilters();
        refresh();
    }

    protected void confirmRemove(final Order item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("order")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text") + "%s",
                "comanda numarul %s", item.getId()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    protected void refresh() {
        final OrderSpecification criteria = new OrderSpecification();
        final Optional<LocalDateTime> startDate = fromDateFilter.getOptionalValue().map(LocalDate::atStartOfDay);
        final Optional<LocalDateTime> endDate = toDateFilter.getOptionalValue().map(val -> val.plusDays(1).atStartOfDay());
        criteria.setStartDate(startDate.orElse(null));
        criteria.setEndDate(endDate.orElse(null));
        criteria.setDoctor(doctorFilter.getOptionalValue().orElse(null));
        criteria.setClinic(clinicFilter.getOptionalValue().orElse(null));
        criteria.setFinalized(finalizedFilter.getOptionalValue().orElse(null));
        criteria.setPaid(paidFilter.getOptionalValue().orElse(null));
        criteria.setId(idFilter.getOptionalValue()
                .filter(StringUtils::isNumeric)
                .map(Long::valueOf).orElse(null));
        criteria.setPatient(patientFilter.getOptionalValue().filter(val -> !val.isEmpty()).orElse(null));
        criteria.setPrice(priceFilter.getOptionalValue()
                .filter(NumberUtils::isCreatable)
                .map(Integer::valueOf).orElse(null));
        grid.refresh(criteria);
    }

    protected void add() {
        UI.getCurrent().navigate(OrderEditPage.class);
    }

    protected void edit(final Order item) {
        UI.getCurrent().navigate(OrderEditPage.class, item.getId());
    }

    private Component createPrintComponent(final Order item) {
        final Icon icon = new Icon(VaadinIcon.PRINT);
        final Button print = new Button();
        print.setIcon(icon);

        final String filename = String.format("order_%s.xls", item.getId());
        final Anchor download = new Anchor(new StreamResource(filename, () -> generateReport(item)), "");
        download.getElement().setAttribute("download", true);
        download.add(print);

        icon.addClassName("dento-grid-icon");
        print.addClassName("dento-grid-action");
        return download;
    }

    private Component createFinalizedComponent(final Order item) {
        final Icon icon = new Icon(item.isFinalized() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isFinalized() ? "green": "red";
        icon.setColor(color);
        return icon;
    }

    private Component createPaidComponent(final Order item) {
        final Icon icon = new Icon(item.isPaid() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
        icon.addClassName("dento-grid-icon");
        final String color = item.isPaid() ? "green": "red";
        icon.setColor(color);
        return icon;
    }

    private InputStream generateReport(final Order item) {

	    InputStream reportStream = null;
        try {
            reportStream = FileUtils.openInputStream(reportService.createOrderReport(item));
        } catch (IOException | CannotGenerateReportException e) {
            log.error("Error generating report for order {}", item.getId());
            Notification.show("", 5000, Notification.Position.BOTTOM_CENTER);
        }
        return reportStream;
    }

    private void remove(final Order item) {
        try {
            dataService.deleteEntity(item.getId(), Order.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting order: {}", item.getId());
        }
        Notification.show(String.format(Localizer.getLocalizedString("confirmRemove.success"),
                item.getId()), 3000, Notification.Position.BOTTOM_CENTER);
    }

    private void clearFilters() {
	    doctorFilter.setItems(dataService.getAll(Doctor.class));
        clinicFilter.setItems(dataService.getAll(Clinic.class));
        patientFilter.setValue("");
        fromDateFilter.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1));
        toDateFilter.setValue(null);
        finalizedFilter.setValue(null);
        paidFilter.setValue(null);
        doctorFilter.setValue(null);
        clinicFilter.setValue(null);
        idFilter.setValue("");
        priceFilter.setValue("");
    }

	protected void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        dateFilterLayout.setWidth("100%");
        finalizedFilter.setItems(Arrays.asList(true, false));
        paidFilter.setItems(Arrays.asList(true, false));
        fromDateFilter.addValueChangeListener(event -> refresh());
        toDateFilter.addValueChangeListener(event -> refresh());
        idFilter.addValueChangeListener(event -> refresh());
        finalizedFilter.addValueChangeListener(event -> refresh());
        paidFilter.addValueChangeListener(event -> refresh());
        patientFilter.addValueChangeListener(event -> refresh());
        doctorFilter.addValueChangeListener(event -> refresh());
        clinicFilter.addValueChangeListener(event -> refresh());
        priceFilter.addValueChangeListener(event -> refresh());

        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        priceFilter.setValueChangeMode(ValueChangeMode.EAGER);
        patientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        dateFilterLayout.add(fromDateFilter, toDateFilter);
        fromDateFilter.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        toDateFilter.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        priceFilter.addClassName("dento-grid-filter-small");
        patientFilter.addClassName("dento-grid-filter-large");
        doctorFilter.addClassName("dento-grid-filter-large");
        clinicFilter.addClassName("dento-grid-filter-large");
        idFilter.addClassName("dento-grid-filter-small");
        finalizedFilter.addClassName("dento-grid-filter-small");
        paidFilter.addClassName("dento-grid-filter-small");
        fromDateFilter.setWidth("65px");
        toDateFilter.setWidth("65px");
    }

	private void initLayout() {
	    final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout menuLayout = new HorizontalLayout(filterButton, addButton, printButton);
        layout.add(menuLayout, grid);
        layout.setHeight("100%");
        layout.setPadding(false);
        layout.setSpacing(false);
        this.setContent(layout);
    }

    private Component createCollectionColumn(final Collection<String> values) {
	    final Div div = new Div();
	    values.stream().map(Paragraph::new).forEach(div::add);
	    return div;
    }
}
