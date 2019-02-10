package com.company.dento.service;

import com.company.dento.model.business.Job;
import com.company.dento.model.business.Order;
import com.company.dento.report.JasperWriter;
import com.company.dento.service.exception.CannotGenerateReportException;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReportService {

    private final JasperWriter jasperWriter;
    private final Resource orderReportTemplate;

    public ReportService(final JasperWriter jasperWriter, final Resource orderReportTemplate) {
        this.jasperWriter = jasperWriter;
        this.orderReportTemplate = orderReportTemplate;
    }

    public File createOrderReport(final Order order) throws CannotGenerateReportException {
        Preconditions.checkNotNull(order, "Order cannot be null");
        log.info("Creating order report {}", order);

        try {
            return jasperWriter.writeReport(orderReportTemplate.getFile(), constructOrderParameters(order));
        } catch (final JRException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File createOrdersReport(final List<Order> orders) throws CannotGenerateReportException {
        log.info("Creating jobs report for {} orders", orders.size());

       return null;
    }

    private Map<String, Object> constructOrderParameters(final Order order) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", "Dento");
        parameters.put("ADDRESS1", "Aici adresa");
        parameters.put("ADDRESS2", "Oras, cod");
        parameters.put("CONTACT", "Telefon, mail");
        parameters.put("CLINIC", order.getClinic().getName());
        parameters.put("DOCTOR", order.getDoctor().toString());
        parameters.put("ORDER_NO", order.getId());
        parameters.put("COLOR", order.getColor().getName());
        parameters.put("DELIVERY_DATE", order.getDeliveryDate().toLocalDate());
        parameters.put("JOBS", order.getJobs().stream().map(j -> j.getTemplate()).collect(Collectors.toList()));
        return parameters;
    }

    private String formatDateTime(final LocalDateTime dateTime) {
        return dateTime != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime) : "";
    }

    private String formatDate(final LocalDate date) {
        return date != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date) : "";
    }

    private List<String> extractJobNames(final Order order) {
        return order.getJobs().stream()
                .map(job -> job.getTemplate().getName())
                .collect(Collectors.toList());
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
        columns.add(extractJobNames(order).stream().collect(Collectors.joining(System.lineSeparator())));
        columns.add(extractPricesPerJob(order));
        columns.add(extractPricesTotal(order));
        columns.add(order.isFinalized() ? "Finalizată" : "Nefinalizată");
        columns.add(order.isPaid() ? "Achitată" : "Neachitată");
        return columns;
    }
}
