package com.company.dento.ui.page.list;

import com.company.dento.dao.specification.MaterialSpecification;
import com.company.dento.model.business.*;
import com.company.dento.service.DataService;
import com.company.dento.service.ReportService;
import com.company.dento.service.exception.CannotGenerateReportException;
import com.company.dento.service.exception.TooManyResultsException;
import com.company.dento.ui.component.common.ConfirmDialog;
import com.company.dento.ui.localization.Localizable;
import com.company.dento.ui.localization.Localizer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@UIScope
@org.springframework.stereotype.Component
@Route(value = "materials")
@Log4j2
public class MaterialsPage extends ListPage<Material, MaterialSpecification> implements Localizable {

	private static final long serialVersionUID = 1L;

    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;
    private final ComboBox<Clinic> clinicFilter;
    private final ComboBox<Doctor> doctorFilter;
    private final ComboBox<JobTemplate> jobFilter;
    private final ComboBox<MaterialTemplate> templateFilter;
    private final ConfirmDialog confirmDialog;
    private final ReportService reportService;
    private final Div totalSumLabel = new Div();

	public MaterialsPage(final DataService dataService, final ReportService reportService) {
	    super(Material.class, dataService, "Consum Materiale");
	    this.reportService = reportService;

        clinicFilter = new ComboBox<>();
        doctorFilter = new ComboBox<>();
        fromDateFilter = new DatePicker();
        toDateFilter = new DatePicker();
        confirmDialog = new ConfirmDialog();
        jobFilter = new ComboBox<>();
        templateFilter = new ComboBox<>();

        grid.addColumn("template.name");
        grid.addColumn("quantity");
        grid.addColumn("price");
        grid.addColumn(m -> m.getQuantity() * m.getPrice()).setKey("totalPrice");

        this.addPrintButton("raport_consum_materiale");

        grid.setNonResponsiveColumns(grid.getColumns().get(0), grid.getColumns().get(1));
        grid.setDetailColumns(grid.getColumns().get(2), grid.getColumns().get(3));

        grid.appendFooterRow().getCells().get(3).setComponent(totalSumLabel);
        totalSumLabel.setWidthFull();
        totalSumLabel.getStyle().set("text-align", "right");
        totalSumLabel.getStyle().set("font-weight", "bold");

        initFilters();
	}
	
	@Override
	public void localize() {
		super.localize();
		confirmDialog.localize();
        jobFilter.setItemLabelGenerator(JobTemplate::getName);
        templateFilter.setItemLabelGenerator(MaterialTemplate::getName);
	}

    protected void confirmRemove(final Material item) {
    }

    protected void refresh() {
        final MaterialSpecification criteria = getCurrentFilter();
        grid.refresh(criteria);
        updateTotal(criteria);
    }

    protected void add() { }

    protected void edit(final Material item) {
    }

    private MaterialSpecification getCurrentFilter() {
        final MaterialSpecification criteria = new MaterialSpecification();
        final Optional<LocalDateTime> startDate = fromDateFilter.getOptionalValue().map(LocalDate::atStartOfDay);
        final Optional<LocalDateTime> endDate = toDateFilter.getOptionalValue().map(val -> val.plusDays(1).atStartOfDay());
        criteria.setStartDate(startDate.orElse(null));
        criteria.setEndDate(endDate.orElse(null));
        criteria.setDoctor(doctorFilter.getOptionalValue().orElse(null));
        criteria.setClinic(clinicFilter.getOptionalValue().orElse(null));
        criteria.setTemplate(templateFilter.getOptionalValue().orElse(null));
        criteria.setJob(jobFilter.getOptionalValue().orElse(null));
        return criteria;
    }

    protected void clearFilters() {
	    doctorFilter.setItems(dataService.getAll(Doctor.class));
        clinicFilter.setItems(dataService.getAll(Clinic.class));
        fromDateFilter.setValue(null);
        toDateFilter.setValue(null);
        doctorFilter.setValue(null);
        clinicFilter.setValue(null);
        templateFilter.setValue(null);
        jobFilter.setValue(null);
    }

    @Override
    protected InputStream createPrintContent() {
        final Map<String, Boolean> sortOrder = new LinkedHashMap<>();
        grid.getSortOrder()
                .forEach(sort -> sortOrder.put(sort.getSorted().getKey(),
                        sort.getDirection() == SortDirection.ASCENDING));

        try {
            return FileUtils.openInputStream(reportService.createMaterialsReport(getCurrentFilter(), sortOrder));
        } catch (CannotGenerateReportException | IOException e) {
            displayReportError("Raportul nu a putut fi generat! Eroare interna!");
            return null;
        } catch (TooManyResultsException e) {
            displayReportError("Prea multe rezultate! Aplicați mai multe filtre!");
            return null;
        }
    }

    private void displayReportError(final String message) {
	    UI.getCurrent().access(() -> Notification.show(message, 5000, Notification.Position.BOTTOM_CENTER));
    }

    protected void initFilters() {
        final HorizontalLayout dateFilterLayout = new HorizontalLayout();
        dateFilterLayout.setMargin(false);
        dateFilterLayout.add(fromDateFilter, toDateFilter);
        fromDateFilter.setWidth("47%");
        toDateFilter.setWidth("47%");
        filterDialog.addFilter("Dată", dateFilterLayout);
        filterDialog.addFilter("Material", templateFilter);
        filterDialog.addFilter("Clinică", clinicFilter);
        filterDialog.addFilter("Doctor", doctorFilter);
        filterDialog.addFilter("Lucrare", jobFilter);
    }

    private void updateTotal(final MaterialSpecification criteria) {
        final Double total = dataService.getMaterialPriceTotal(criteria);
        totalSumLabel.setText(String.format(Localizer.getCurrentLocale(), "Total: %.2f", total));
    }
}
