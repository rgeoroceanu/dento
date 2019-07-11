package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.OrderSpecification;
import com.company.dento.model.business.Job;
import com.company.dento.model.business.Order;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.service.ReportService;
import com.company.dento.service.exception.CannotGenerateReportException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jni.Local;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@UIScope
@org.springframework.stereotype.Component
@Route(value = "executions")
@Log4j2
public class ExecutionsPage extends ListPage<Order, OrderSpecification> implements Localizable {

    private static final long serialVersionUID = 1L;

    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;
    private final ComboBox<User> technicianFilter;
    private final ComboBox<Boolean> finalizedFilter;
    private final ConfirmDialog confirmDialog;
    private final ReportService reportService;

    private final Label jobTypeLabel = new Label("Tip Lucrare");
    private final Label executionNameLabel = new Label("Nume Manoperă");
    private final Label countLabel = new Label("Cantitate");
    private final Label pricePerElementLabel = new Label("Valoare Unitară");
    private final Label priceTotalLabel = new Label("Valoare Totală");
    private final Div totalSumLabel = new Div();

    public ExecutionsPage(final DataService dataService, final ReportService reportService) {
        super(Order.class, dataService, "Manopere Tehnicieni");
        this.reportService = reportService;

        technicianFilter = new ComboBox<>();
        fromDateFilter = new DatePicker();
        toDateFilter = new DatePicker();
        finalizedFilter = new ComboBox<>();
        confirmDialog = new ConfirmDialog();
        technicianFilter.setPreventInvalidInput(true);
        technicianFilter.setAllowCustomValue(false);

        final HorizontalLayout jobsHeader = new HorizontalLayout(jobTypeLabel, executionNameLabel,
                countLabel, pricePerElementLabel, priceTotalLabel);

        jobsHeader.getStyle().set("padding-left", "16px");
        jobTypeLabel.setWidth("25%");
        executionNameLabel.setWidth("25%");
        countLabel.setWidth("15%");
        pricePerElementLabel.setWidth("15%");
        priceTotalLabel.setWidth("15%");

        grid.addColumn(new LocalDateRenderer<>(Order::getDate, "d.M.yyyy"))
                .setKey("date");

        grid.addColumn("id");
        grid.addColumn("patient");
        grid.addColumn("clinic.name");
        grid.addColumn("doctor");

        grid.addComponentColumn(item -> createJobsColumn(item.getJobs()))
                .setWidth("40%").setHeader(jobsHeader);

        grid.appendFooterRow().getCells().get(5).setComponent(totalSumLabel);
        totalSumLabel.setWidthFull();
        totalSumLabel.getStyle().set("text-align", "right");
        totalSumLabel.getStyle().set("font-weight", "bold");

        this.addPrintButton("raport_manopere_tehnicieni");
        this.addButton.setVisible(false);

        //grid.setHeightByRows(true);
        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));
        grid.setDetailColumns(grid.getColumns().get(2), grid.getColumns().get(3), grid.getColumns().get(4));

        initFilters();
    }

    @Override
    public void localize() {
        super.localize();
        confirmDialog.localize();
        finalizedFilter.setItemLabelGenerator(item ->
                item ? Localizer.getLocalizedString("yes") : Localizer.getLocalizedString("no"));
    }

    protected void confirmRemove(final Order item) { }

    protected void refresh() {
        final OrderSpecification criteria = new OrderSpecification();
        final Optional<LocalDateTime> startDate = fromDateFilter.getOptionalValue().map(LocalDate::atStartOfDay);
        final Optional<LocalDateTime> endDate = toDateFilter.getOptionalValue().map(val -> val.plusDays(1).atStartOfDay());
        criteria.setStartDate(startDate.orElse(null));
        criteria.setEndDate(endDate.orElse(null));
        criteria.setFinalized(finalizedFilter.getOptionalValue().orElse(null));
        criteria.setTechnician(technicianFilter.getOptionalValue().orElse(null));
        criteria.setWithExecutions(true);

        grid.refresh(criteria);
        updateTotal(criteria);
    }

    protected void add() { }

    protected void edit(final Order item) {}

    protected void clearFilters() {
        technicianFilter.setItems(dataService.getAllTechnicians());
        fromDateFilter.setValue(null);
        toDateFilter.setValue(null);
        finalizedFilter.setValue(null);
        technicianFilter.setValue(null);
    }

    @Override
    protected InputStream createPrintContent() {
        try {
            return FileUtils.openInputStream(reportService
                    .createOrdersReport(grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList())));
        } catch (CannotGenerateReportException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        dateFilterLayout.setMargin(false);
        finalizedFilter.setItems(Arrays.asList(true, false));
        dateFilterLayout.add(fromDateFilter, toDateFilter);
        fromDateFilter.setWidth("47%");
        toDateFilter.setWidth("47%");
        filterDialog.addFilter("Data", dateFilterLayout);
        filterDialog.addFilter("Tehnician", technicianFilter);
        filterDialog.addFilter("Finalizat", finalizedFilter);
    }

    private Component createJobsColumn(final Collection<Job> jobs) {
        final Grid<Job> grid = new Grid<>(Job.class);
        grid.removeAllColumns();
        grid.addColumn("template.name").setWidth("25%");;
        grid.addComponentColumn( j -> createCollectionColumn(j.getExecutions().stream()
                .map(e -> e.getTemplate().getName())
                .collect(Collectors.toList())))
                .setWidth("25%");

        grid.addComponentColumn( j -> createCollectionColumn(j.getExecutions().stream()
                .map(e ->  String.format(Localizer.getCurrentLocale(), "%d", e.getCount()))
                .collect(Collectors.toList())))
                .setWidth("15%");

        grid.addComponentColumn( j -> createCollectionColumn(j.getExecutions().stream()
                .map(e ->  String.format(Localizer.getCurrentLocale(), "%.2f", e.getPrice()))
                .collect(Collectors.toList())))
                .setWidth("15%");

        grid.addComponentColumn( j -> createCollectionColumn(j.getExecutions().stream()
                .map(e ->  String.format(Localizer.getCurrentLocale(), "%.2f", e.getCount() * e.getPrice()))
                .collect(Collectors.toList())))
                .setWidth("15%");

        grid.setItems(jobs);
        grid.addClassNames("dento-grid", "dento-no-header-grid");
        grid.getElement().setAttribute("theme", "no-border row-stripes");
        grid.setHeightByRows(true);
        return grid;
    }

    private Component createCollectionColumn(final Collection<String> values) {
        final Div div = new Div();
        values.stream().map(Paragraph::new).forEach(div::add);
        return div;
    }

    private void updateTotal(final OrderSpecification criteria) {
        final Double total = dataService.getExecutionsPriceTotal(criteria);
        totalSumLabel.setText(String.format(Localizer.getCurrentLocale(),"Total: %.2f", total));
    }
}
