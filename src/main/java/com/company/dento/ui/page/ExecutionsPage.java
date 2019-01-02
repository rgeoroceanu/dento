package com.company.dento.ui.page;

import com.company.dento.dao.specification.ExecutionSpecification;
import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.component.common.FilterableGrid;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.HeaderRow;
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
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@UIScope
@Secured(value = {"USER", "ADMIN"})
@Route(value = "executions")
@Log4j2
public class ExecutionsPage extends Page implements Localizable, AfterNavigationObserver {
	
	private static final long serialVersionUID = 1L;
	private final FilterableGrid<Execution, ExecutionSpecification> grid;
    private final ComboBox<User> technicianFilter;
    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;
    private final ComboBox<Boolean> finalizedFilter;
    private final TextField orderIdFilter;
    private final ComboBox<String> templateNameFilter;
    private final TextField countFilter;
    private final TextField priceFilter;
    private final ConfirmDialog confirmDialog;

	public ExecutionsPage(final DataService dataService) {
	    super(dataService);
		grid = new FilterableGrid<>(Execution.class, dataService);

        technicianFilter = new ComboBox<>();
        fromDateFilter = new DatePicker();
        toDateFilter = new DatePicker();
        finalizedFilter = new ComboBox<>();
        orderIdFilter = new TextField();
        templateNameFilter = new ComboBox<>();
        countFilter = new TextField();
        priceFilter = new TextField();
        confirmDialog = new ConfirmDialog();

        grid.addColumn(new LocalDateRenderer<>(item -> item.getCreated().toLocalDate(), "d.M.yyyy"))
                .setKey("date");

        grid.addColumn("job.order.id");
        grid.addColumn("template.name");
        grid.addColumn("technician");
        grid.addColumn("count");
        grid.addColumn("price");

        grid.addComponentColumn(item -> {
            final Icon icon = new Icon(item.getJob().getOrder().isFinalized() ? VaadinIcon.CHECK : VaadinIcon.CLOSE_SMALL);
            icon.addClassName("dento-grid-icon");
            final String color = item.getJob().getOrder().isFinalized() ? "green": "red";
            icon.setColor(color);
            return icon;
        }).setKey("jobFinalized");

        grid.addComponentColumn(item -> {
            final Icon icon = new Icon(VaadinIcon.TRASH);
            final Button remove = new Button();
            remove.setIcon(icon);
            remove.addClickListener(e -> confirmRemove(item));
            icon.addClassName("dento-grid-icon");
            remove.addClassName("dento-grid-action");
            return remove;
        }).setKey("remove").setWidth("18px");

        initFilters();
        initLayout();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
        finalizedFilter.setItemLabelGenerator(item ->
                item ? Localizer.getLocalizedString("yes") : Localizer.getLocalizedString("no"));
	}

    @Override
    public void afterNavigation(final AfterNavigationEvent event) {
        clearFilters();
        refresh();
    }

    private void confirmRemove(final Execution item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("execution")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                item.getTemplate().getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    private void remove(final Execution item) {
        try {
            dataService.deleteEntity(item.getId(), Execution.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting execution: {}", item.getId());
        }
        Notification.show(String.format(Localizer.getLocalizedString("confirmRemove.success"),
                item.getTemplate().getName()), 3000, Notification.Position.BOTTOM_CENTER);
    }

    private void refresh() {
	    final ExecutionSpecification criteria = new ExecutionSpecification();
	    final Optional<LocalDateTime> startDate = fromDateFilter.getOptionalValue().map(LocalDate::atStartOfDay);
        final Optional<LocalDateTime> endDate = toDateFilter.getOptionalValue().map(val -> val.plusDays(1).atStartOfDay());
	    criteria.setStartDate(startDate.orElse(null));
        criteria.setEndDate(endDate.orElse(null));
        criteria.setTechnician(technicianFilter.getOptionalValue().orElse(null));
        criteria.setFinalized(finalizedFilter.getOptionalValue().orElse(null));
        criteria.setOrderId(orderIdFilter.getOptionalValue()
                .filter(StringUtils::isNumeric)
                .map(Long::valueOf).orElse(null));
        criteria.setTemplateName(templateNameFilter.getOptionalValue().orElse(null));
        criteria.setCount(countFilter.getOptionalValue()
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf).orElse(null));
        criteria.setPrice(priceFilter.getOptionalValue()
                .filter(NumberUtils::isCreatable)
                .map(Integer::valueOf).orElse(null));
        grid.refresh(criteria);
    }
    

    private void clearFilters() {
	    technicianFilter.setItems(dataService.getAll(User.class));
        templateNameFilter.setItems(dataService.getAll(ExecutionTemplate.class).stream().map(ExecutionTemplate::getName));
        technicianFilter.setValue(null);
        fromDateFilter.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1));
        toDateFilter.setValue(null);
        finalizedFilter.setValue(false);
        templateNameFilter.setValue(null);
        countFilter.setValue("");
        orderIdFilter.setValue("");
        priceFilter.setValue("");
        finalizedFilter.setValue(null);
    }

	private void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        finalizedFilter.setItems(Arrays.asList(true, false));
        fromDateFilter.addValueChangeListener(event -> refresh());
        toDateFilter.addValueChangeListener(event -> refresh());
        technicianFilter.addValueChangeListener(event -> refresh());
        finalizedFilter.addValueChangeListener(event -> refresh());
        orderIdFilter.addValueChangeListener(event -> refresh());
        templateNameFilter.addValueChangeListener(event -> refresh());
        countFilter.addValueChangeListener(event -> refresh());
        priceFilter.addValueChangeListener(event -> refresh());
        orderIdFilter.setValueChangeMode(ValueChangeMode.EAGER);
        priceFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        dateFilterLayout.add(fromDateFilter, toDateFilter);

        final HeaderRow filterRow = grid.appendHeaderRow();
        filterRow.getCells().get(0).setComponent(dateFilterLayout);
        filterRow.getCells().get(1).setComponent(orderIdFilter);
        filterRow.getCells().get(2).setComponent(templateNameFilter);
        filterRow.getCells().get(3).setComponent(technicianFilter);
        filterRow.getCells().get(4).setComponent(countFilter);
        filterRow.getCells().get(5).setComponent(priceFilter);
        filterRow.getCells().get(6).setComponent(finalizedFilter);
        fromDateFilter.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        toDateFilter.addClassNames("dento-grid-filter-small", "dento-grid-date-picker");
        priceFilter.addClassName("dento-grid-filter-small");
        technicianFilter.addClassName("dento-grid-filter-large");
        templateNameFilter.addClassName("dento-grid-filter-large");
        orderIdFilter.addClassName("dento-grid-filter-small");
        countFilter.addClassName("dento-grid-filter-small");
        finalizedFilter.addClassName("dento-grid-filter-small");
        fromDateFilter.setWidth("75px");
        toDateFilter.setWidth("75px");
    }

	private void initLayout() {
	    final VerticalLayout layout = new VerticalLayout();
        layout.add(grid);
        layout.setHeight("100%");
        layout.setPadding(false);
        this.setContent(layout);
    }
}
