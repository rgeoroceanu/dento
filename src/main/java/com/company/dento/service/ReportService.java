package com.company.dento.service;

import com.company.dento.model.business.Order;
import com.company.dento.service.exception.CannotGenerateReportException;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReportService {

    private static final String TEMPLATES_FOLDER = "classpath:templates/";
    private static final String ORDER_TEMPLATE = TEMPLATES_FOLDER + "order_template.xlt";
    private static final String ORDER_ID_PLACEHOLDER = "%OrderId%";
    private static final String DATE_PLACEHOLDER = "%Date%";
    private static final String CLINIC_PLACEHOLDER = "%Clinic%";
    private static final String DOCTOR_PLACEHOLDER = "%Doctor%";
    private static final String PACIENT_PLACEHOLDER = "%Pacient%";
    private static final String COLOR_PLACEHOLDER = "%Color%";
    private static final String DELIVERY_DATE_PLACEHOLDER = "%DeliveryDate%";
    private static final String OBSERVATIONS_PLACEHOLDER = "%Observations%";
    private static final String JOBS_PLACEHOLDER = "%Jobs%";

    public File createOrderReport(final Order order) throws CannotGenerateReportException {
        log.info("Creating order report {}", order);

        final File template;
        final Workbook workbook;

        try {
            template = ResourceUtils.getFile(ORDER_TEMPLATE);
            workbook = openTemplate(template);
        } catch (final FileNotFoundException e) {
            log.error("Error loading template", e);
            throw new CannotGenerateReportException("Error loading template", e);
        } catch (final IOException e) {
            log.error("Error generating report", e);
            throw new CannotGenerateReportException("Error generating report", e);
        }

        replaceCellText(workbook, ORDER_ID_PLACEHOLDER, String.valueOf(order.getId()), "P2");
        replaceCellText(workbook, DATE_PLACEHOLDER, formatDate(order.getDate()), "P3");
        replaceCellText(workbook, CLINIC_PLACEHOLDER, order.getClinic().getName(), "B8");
        replaceCellText(workbook, DOCTOR_PLACEHOLDER, String.format("%s %s", order.getDoctor().getFirstName(),
                order.getDoctor().getLastName()), "B9");
        replaceCellText(workbook, PACIENT_PLACEHOLDER, order.getPatient(), "L8");
        replaceCellText(workbook, COLOR_PLACEHOLDER, order.getColor().getName(), "A21");
        replaceCellText(workbook, DELIVERY_DATE_PLACEHOLDER, formatDateTime(order.getDeliveryDate()), "A22");
        replaceCellText(workbook, OBSERVATIONS_PLACEHOLDER, order.getDescription(), "J19");

        final String jobs = order.getJobs().stream()
                .map(job -> job.getTemplate().getName())
                .collect(Collectors.joining(System.lineSeparator()));

        replaceCellText(workbook, JOBS_PLACEHOLDER, jobs, "B20");

        final File report;
        try {
            report = saveWorkbook(workbook);
        } catch (final IOException e) {
            log.error("Error saving report", e);
            throw new CannotGenerateReportException("Error saving report", e);
        }

        return report;
    }

    private File saveWorkbook(final Workbook workbook) throws IOException {
        final File destination = Files.createTempFile("report", ".xlsx").toFile();
        final FileOutputStream out = new FileOutputStream(destination);
        workbook.write(out);
        out.close();
        return destination;
    }

    private Workbook openTemplate(final File templateFile) throws IOException {
        final FileInputStream inputStream = new FileInputStream(templateFile);
        return new HSSFWorkbook(inputStream);
    }

    private void replaceCellText(final Workbook workbook, final String placeholder,
                                 final String text, final String cellId) {

        final Cell cell = getCellById(workbook, cellId);
        final String cellText = cell.getStringCellValue();
        final String updated = cellText.replace(placeholder, text);
        cell.setCellValue(updated);
    }

    private Cell getCellById(final Workbook workbook, final String id) {
        final CellReference cr = new CellReference(id);
        final Row row = workbook.getSheetAt(0).getRow(cr.getRow());
        return row.getCell(cr.getCol());
    }

    private String formatDateTime(final LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime);
    }

    private String formatDate(final LocalDate date) {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date);
    }
}
