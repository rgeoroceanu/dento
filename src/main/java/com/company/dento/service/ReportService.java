package com.company.dento.service;

import com.company.dento.model.business.Job;
import com.company.dento.model.business.Order;
import com.company.dento.report.ExcelWriter;
import com.company.dento.service.exception.CannotGenerateReportException;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReportService {

    private static final String TEMPLATES_FOLDER = "classpath:templates/";
    private static final String ORDER_TEMPLATE = TEMPLATES_FOLDER + "order_template.xlt";
    private static final String JOBS_REPORT_TEMPLATE = TEMPLATES_FOLDER + "jobs_report_template.xlt";
    private static final String ORDER_ID_PLACEHOLDER = "%OrderId%";
    private static final String DATE_PLACEHOLDER = "%Date%";
    private static final String CLINIC_PLACEHOLDER = "%Clinic%";
    private static final String DOCTOR_PLACEHOLDER = "%Doctor%";
    private static final String PACIENT_PLACEHOLDER = "%Pacient%";
    private static final String COLOR_PLACEHOLDER = "%Color%";
    private static final String DELIVERY_DATE_PLACEHOLDER = "%DeliveryDate%";
    private static final String OBSERVATIONS_PLACEHOLDER = "%Observations%";
    private static final String JOBS_PLACEHOLDER = "%Jobs%";
    private static final List<String> SAMPLES_TABLE_HEADER = Arrays.asList("Proba", "Lucrare", "Data");
    private static final List<String> EXECUTIONS_TABLE_HEADER = Arrays.asList("Manoperă", "Lucrare", "Tehnician");
    private static final List<String> JOBS_REPORT_TABLE_HEADER = Arrays.asList("Dată", "Nr.Fișă", "Pacient", "Clinicä / Doctor",
            "Preț", "Tip Lucrare", "Preț pe Element", "Preț pe Lucrare", "Stadiu", "Status");

    private final ExcelWriter excelWriter;

    public ReportService(final ExcelWriter excelWriter) {
        this.excelWriter = excelWriter;
    }

    public File createOrderReport(final Order order) throws CannotGenerateReportException {
        log.info("Creating order report {}", order);

        final Workbook workbook = openTemplateWorkbook(ORDER_TEMPLATE);

        excelWriter.replaceCellText(workbook, ORDER_ID_PLACEHOLDER, String.valueOf(order.getId()), "P2");
        excelWriter.replaceCellText(workbook, DATE_PLACEHOLDER, formatDate(order.getDate()), "P3");
        excelWriter.replaceCellText(workbook, CLINIC_PLACEHOLDER, order.getClinic().getName(), "B8");
        excelWriter.replaceCellText(workbook, DOCTOR_PLACEHOLDER, String.format("%s %s", order.getDoctor().getFirstName(),
                order.getDoctor().getLastName()), "B9");
        excelWriter.replaceCellText(workbook, PACIENT_PLACEHOLDER, order.getPatient(), "L8");
        excelWriter.replaceCellText(workbook, COLOR_PLACEHOLDER, order.getColor() != null ? order.getColor().getName() : "", "A21");
        excelWriter.replaceCellText(workbook, DELIVERY_DATE_PLACEHOLDER, formatDateTime(order.getDeliveryDate()), "A22");
        excelWriter.replaceCellText(workbook, OBSERVATIONS_PLACEHOLDER, order.getDescription(), "J19");

        excelWriter.replaceCellText(workbook, JOBS_PLACEHOLDER, extractJobNames(order), "B20");

        excelWriter.createTable(workbook, 25, SAMPLES_TABLE_HEADER, extractSampleRows(order));

        excelWriter.createTable(workbook, workbook.getSheetAt(0).getLastRowNum() + 2,
                EXECUTIONS_TABLE_HEADER, extractExecutionRows(order));

        return saveWorkbook(workbook);
    }

    public File createOrdersReport(final List<Order> orders) throws CannotGenerateReportException {
        log.info("Creating jobs report for {} orders", orders.size());

        final Workbook workbook = openTemplateWorkbook(JOBS_REPORT_TEMPLATE);

        excelWriter.createTable(workbook, 9, JOBS_REPORT_TABLE_HEADER, extractOrdersRows(orders));

        return saveWorkbook(workbook);
    }

    private Workbook openTemplateWorkbook(final String templatePath) throws CannotGenerateReportException {
        final File template;
        final Workbook workbook;

        try {
            template = ResourceUtils.getFile(templatePath);
            workbook = excelWriter.openTemplate(template);
        } catch (final FileNotFoundException e) {
            log.error("Error loading template", e);
            throw new CannotGenerateReportException("Error loading template", e);
        } catch (final IOException e) {
            log.error("Error generating report", e);
            throw new CannotGenerateReportException("Error generating report", e);
        }

        return workbook;
    }

    private File saveWorkbook(final Workbook workbook) throws CannotGenerateReportException {
        final File report;
        try {
            report = excelWriter.saveWorkbook(workbook);
        } catch (final IOException e) {
            log.error("Error saving report", e);
            throw new CannotGenerateReportException("Error saving report", e);
        }

        return report;
    }

    private String formatDateTime(final LocalDateTime dateTime) {
        return dateTime != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime) : "";
    }

    private String formatDate(final LocalDate date) {
        return date != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date) : "";
    }

    private String extractJobNames(final Order order) {
        return order.getJobs().stream()
                .map(job -> job.getTemplate().getName())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String extractPricesPerJob(final Order order) {
        return order.getJobs().stream()
                .map(job -> String.valueOf(job.getPrice()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String extractPricesTotal(final Order order) {
        return order.getJobs().stream()
                .map(job -> String.valueOf(job.getPrice() * job.getCount()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private List<List<String>> extractSampleRows(final Order order) {
        return order.getJobs().stream()
                .map(Job::getSamples)
                .flatMap(Collection::stream)
                .map(sample -> Arrays.asList(sample.getTemplate().getName(),
                        sample.getJob().getTemplate().getName(),
                        formatDate(sample.getDate())))
                .collect(Collectors.toList());
    }

    private List<List<String>> extractExecutionRows(final Order order) {
        return order.getJobs().stream()
                .map(Job::getExecutions)
                .flatMap(Collection::stream)
                .map(ex -> Arrays.asList(ex.getTemplate().getName(),
                        ex.getJob().getTemplate().getName(),
                        ex.getTechnician() != null ? ex.getTechnician().toString() : ""))
                .collect(Collectors.toList());
    }

    private List<List<String>> extractOrdersRows(final List<Order> orders) {
        return orders.stream()
                .map(this::extractOrderColumns)
                .collect(Collectors.toList());
    }

    private List<String> extractOrderColumns(final Order order) {
        final List<String> columns = new ArrayList<>();
        columns.add(formatDate(order.getDate()));
        columns.add(String.format("%d", order.getId()));
        columns.add(order.getPatient());
        columns.add(String.format("%s%n%s", order.getClinic().getName(), order.getDoctor().toString()));
        columns.add(String.format("%d", order.getPrice()));
        columns.add(extractJobNames(order));
        columns.add(extractPricesPerJob(order));
        columns.add(extractPricesTotal(order));
        columns.add(order.isFinalized() ? "Finalizată" : "Nefinalizată");
        columns.add(order.isPaid() ? "Achitată" : "Neachitată");
        return columns;
    }
}
