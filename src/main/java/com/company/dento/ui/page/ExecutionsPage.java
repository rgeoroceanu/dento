package com.company.dento.ui.page;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionCriteria;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.ui.component.common.FilterableGrid;
import com.company.dento.ui.localization.Localizable;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
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
    private final DatePicker fromDate;
    private final DatePicker toDate;
    private final Checkbox finalized;

	public ExecutionsPage(final DataService dataService) {
	    super(dataService);
		grid = new FilterableGrid<>(Execution.class, dataService);

        technicianFilter = new ComboBox<>();
        fromDate = new DatePicker();
        toDate = new DatePicker();
        finalized = new Checkbox();

        grid.removeColumnByKey("count");
        grid.removeColumnByKey("price");
        grid.removeColumnByKey("job");
        grid.removeColumnByKey("template");
        grid.addColumn("job.order.id").setWidth("50px");
        grid.addColumn("job.template.name");
        grid.addColumn("template.name");
        grid.addColumn("job.technician");
        grid.addColumn("template.coefficient").setWidth("50px");
        grid.addColumn("count").setWidth("50px");
        grid.addColumn("price").setWidth("50px");
        grid.addColumn("job.finalized").setWidth("70px");

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
	    final Optional<LocalDateTime> startDate = fromDate.getOptionalValue().map(LocalDate::atStartOfDay);
        final Optional<LocalDateTime> endDate = toDate.getOptionalValue().map(val -> val.plusDays(1).atStartOfDay());
        final Optional<User> technician = technicianFilter.getOptionalValue();
	    criteria.setStartDate(startDate.orElse(null));
        criteria.setEndDate(endDate.orElse(null));
        criteria.setTechnician(technician.orElse(null));
        criteria.setFinalized(finalized.getValue());
        grid.refresh(criteria);
    }

    private void clearFilters() {
	    technicianFilter.setItems(dataService.getAll(User.class));
        technicianFilter.setValue(null);
        fromDate.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1));
        toDate.setValue(null);
        finalized.setValue(false);
    }

	private void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        fromDate.addValueChangeListener(event -> refresh());
        toDate.addValueChangeListener(event -> refresh());
        technicianFilter.addValueChangeListener(event -> refresh());
        finalized.addValueChangeListener(event -> refresh());
        dateFilterLayout.add(fromDate, toDate);
        final HeaderRow filterRow = grid.appendHeaderRow();
        filterRow.getCells().get(0).setComponent(dateFilterLayout);
        filterRow.getCells().get(4).setComponent(technicianFilter);
        filterRow.getCells().get(8).setComponent(finalized);
        fromDate.setWidth("140px");
        toDate.setWidth("140px");
        technicianFilter.setWidth("170px");
    }

	private void initLayout() {
	    final VerticalLayout layout = new VerticalLayout();
        layout.add(grid);
        layout.setHeight("100%");
        this.setContent(layout);
    }
}
