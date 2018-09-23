package com.company.dento.ui.page;

import com.company.dento.model.business.*;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.FilterableGrid;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "executions")
public class ExecutionsPage extends Page implements Localizable, AfterNavigationObserver {
	
	private static final long serialVersionUID = 1L;
	private final FilterableGrid<Execution, ExecutionCriteria> grid;
    private final ComboBox<User> technicianFilter;
    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;
    private final Checkbox finalizedFilter;
    private final TextField orderIdFilter;
    private final ComboBox<String> jobTemplateNameFilter;
    private final ComboBox<String> templateNameFilter;
    private final TextField countFilter;
    private final TextField fromPriceFilter;
    private final TextField toPriceFilter;

	public ExecutionsPage(final DataService dataService) {
	    super(dataService);
		grid = new FilterableGrid<>(Execution.class, dataService);

        technicianFilter = new ComboBox<>();
        fromDateFilter = new DatePicker();
        toDateFilter = new DatePicker();
        finalizedFilter = new Checkbox();
        orderIdFilter = new TextField();
        jobTemplateNameFilter = new ComboBox<>();
        templateNameFilter = new ComboBox<>();
        countFilter = new TextField();
        fromPriceFilter = new TextField();
        toPriceFilter = new TextField();

        grid.addColumn("job.order.id").setWidth("50px");
        grid.addColumn("job.template.name");
        grid.addColumn("template.name");
        grid.addColumn("job.technician");
        grid.addColumn("count").setWidth("50px");
        grid.addColumn("price");
        grid.addComponentColumn(item -> {
            final Icon icon = new Icon(item.getJob().isFinalized() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
            icon.addClassName("dento-grid-icon");
            final String color = item.getJob().isFinalized() ? "green": "red";
            icon.setColor(color);
            return icon;
        }).setKey("jobFinalized").setWidth("70px");

        initFilters();
        initLayout();
	}
	
	@Override
	public void localize() {
		super.localize();
	}

    @Override
    public void afterNavigation(final AfterNavigationEvent event) {
        clearFilters();
        refresh();
    }

    private void refresh() {
	    final ExecutionCriteria criteria = new ExecutionCriteria();
	    final Optional<LocalDateTime> startDate = fromDateFilter.getOptionalValue().map(LocalDate::atStartOfDay);
        final Optional<LocalDateTime> endDate = toDateFilter.getOptionalValue().map(val -> val.plusDays(1).atStartOfDay());
	    criteria.setStartDate(startDate.orElse(null));
        criteria.setEndDate(endDate.orElse(null));
        criteria.setTechnician(technicianFilter.getOptionalValue().orElse(null));
        criteria.setFinalized(finalizedFilter.getValue());
        criteria.setOrderId(orderIdFilter.getOptionalValue()
                .filter(StringUtils::isNumeric)
                .map(Long::valueOf).orElse(null));
        criteria.setJobTemplateName(jobTemplateNameFilter.getOptionalValue().orElse(null));
        criteria.setTemplateName(templateNameFilter.getOptionalValue().orElse(null));
        criteria.setCount(countFilter.getOptionalValue()
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf).orElse(null));
        criteria.setFromPrice(fromPriceFilter.getOptionalValue()
                .filter(NumberUtils::isCreatable)
                .map(Integer::valueOf).orElse(null));
        criteria.setToPrice(toPriceFilter.getOptionalValue()
                .filter(NumberUtils::isCreatable)
                .map(Integer::valueOf).orElse(null));
        grid.refresh(criteria);
    }
    

    private void clearFilters() {
	    technicianFilter.setItems(dataService.getAll(User.class));
	    jobTemplateNameFilter.setItems(dataService.getAll(JobTemplate.class).stream().map(JobTemplate::getName));
        templateNameFilter.setItems(dataService.getAll(ExecutionTemplate.class).stream().map(ExecutionTemplate::getName));
        technicianFilter.setValue(null);
        //fromDateFilter.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1));
        toDateFilter.setValue(null);
        finalizedFilter.setValue(false);
        jobTemplateNameFilter.setValue(null);
        templateNameFilter.setValue(null);
        countFilter.setValue("");
        orderIdFilter.setValue("");
        fromPriceFilter.setValue("");
        toPriceFilter.setValue("");
    }

	private void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        final HorizontalLayout priceFilterLayout = new HorizontalLayout();
        fromDateFilter.addValueChangeListener(event -> refresh());
        toDateFilter.addValueChangeListener(event -> refresh());
        technicianFilter.addValueChangeListener(event -> refresh());
        finalizedFilter.addValueChangeListener(event -> refresh());
        orderIdFilter.addValueChangeListener(event -> refresh());
        jobTemplateNameFilter.addValueChangeListener(event -> refresh());
        templateNameFilter.addValueChangeListener(event -> refresh());
        countFilter.addValueChangeListener(event -> refresh());
        fromPriceFilter.addValueChangeListener(event -> refresh());
        toPriceFilter.addValueChangeListener(event -> refresh());
        orderIdFilter.setValueChangeMode(ValueChangeMode.EAGER);
        fromPriceFilter.setValueChangeMode(ValueChangeMode.EAGER);
        toPriceFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        dateFilterLayout.add(fromDateFilter, toDateFilter);
        priceFilterLayout.add(fromPriceFilter, toPriceFilter);
        final HeaderRow filterRow = grid.appendHeaderRow();
        filterRow.getCells().get(0).setComponent(dateFilterLayout);
        filterRow.getCells().get(1).setComponent(orderIdFilter);
        filterRow.getCells().get(2).setComponent(jobTemplateNameFilter);
        filterRow.getCells().get(3).setComponent(templateNameFilter);
        filterRow.getCells().get(4).setComponent(technicianFilter);
        filterRow.getCells().get(5).setComponent(countFilter);
        filterRow.getCells().get(6).setComponent(priceFilterLayout);
        filterRow.getCells().get(7).setComponent(finalizedFilter);
        fromDateFilter.setWidth("120px");
        toDateFilter.setWidth("120px");
        fromPriceFilter.setWidth("80px");
        toPriceFilter.setWidth("80px");
        technicianFilter.setWidth("170px");
        jobTemplateNameFilter.setWidth("170px");
        templateNameFilter.setWidth("170px");
        orderIdFilter.setWidth("50px");
        countFilter.setWidth("50px");
    }

	private void initLayout() {
	    final VerticalLayout layout = new VerticalLayout();
        layout.add(grid);
        layout.setHeight("100%");
        this.setContent(layout);
    }
}
