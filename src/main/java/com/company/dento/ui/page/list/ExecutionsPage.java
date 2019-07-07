package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.ExecutionSpecification;
import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.User;
import com.company.dento.service.DataService;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UIScope
@Component
@Route(value = "executions")
@Log4j2
public class ExecutionsPage extends ListPage<Execution, ExecutionSpecification> implements Localizable {
	
	private static final long serialVersionUID = 1L;

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
	    super(Execution.class, dataService, "Manopere");

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
        grid.addColumn("template.name");

        grid.addColumn("job.order.id");
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

        addRemoveColumn();
        addButton.setVisible(false);

        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));

        initFilters();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
        finalizedFilter.setItemLabelGenerator(item ->
                item ? Localizer.getLocalizedString("yes") : Localizer.getLocalizedString("no"));
	}

    @Override
    protected void add() { }

    protected void confirmRemove(final Execution item) {
	    confirmDialog.setHeader(String.format(Localizer.getLocalizedString("confirmRemove.header"),
                Localizer.getLocalizedString("execution")));
        confirmDialog.setText(String.format(Localizer.getLocalizedString("confirmRemove.text"),
                item.getTemplate().getName()));
	    confirmDialog.addConfirmListener(e -> remove(item));
	    confirmDialog.open();
    }

    @Override
    protected void edit(Execution item) { }

    private void remove(final Execution item) {
        try {
            dataService.deleteEntity(item.getId(), Execution.class);
        } catch (DataDoesNotExistException e) {
            log.warn("Tried to delete non-nexisting execution: {}", item.getId());
        }
        Notification.show(String.format(Localizer.getLocalizedString("confirmRemove.success"),
                item.getTemplate().getName()), 3000, Notification.Position.BOTTOM_CENTER);
    }

    protected void refresh() {
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
    

    protected void clearFilters() {
	    technicianFilter.setItems(dataService.getAll(User.class));
        templateNameFilter.setItems(dataService.getAll(ExecutionTemplate.class).stream().map(ExecutionTemplate::getName));
        technicianFilter.setValue(null);
        fromDateFilter.setValue(null);
        toDateFilter.setValue(null);
        finalizedFilter.setValue(false);
        templateNameFilter.setValue(null);
        countFilter.setValue("");
        orderIdFilter.setValue("");
        priceFilter.setValue("");
        finalizedFilter.setValue(null);
    }

    @Override
    protected InputStream createPrintContent() {
        return null;
    }

    protected void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        dateFilterLayout.setMargin(false);
        fromDateFilter.setWidth("47%");
        toDateFilter.setWidth("47%");
        dateFilterLayout.add(fromDateFilter, toDateFilter);
        finalizedFilter.setItems(Arrays.asList(true, false));

        filterDialog.addFilter("Data", dateFilterLayout);
        filterDialog.addFilter("Nume", templateNameFilter);
        filterDialog.addFilter("Id Comanda", orderIdFilter);
        filterDialog.addFilter("Tehnician", technicianFilter);
        filterDialog.addFilter("Cantitate", countFilter);
        filterDialog.addFilter("Pret", priceFilter);
        filterDialog.addFilter("Finalizata", finalizedFilter);
    }
}
