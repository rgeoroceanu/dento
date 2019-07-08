package com.company.dento.service;

import com.company.dento.model.business.*;
import com.company.dento.report.JasperWriter;
import com.company.dento.service.exception.CannotGenerateReportException;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Log4j2
public class ReportService {

    private final JasperWriter jasperWriter;
    private final Resource orderReportTemplate;
    private final DataService dataService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ReportService(final JasperWriter jasperWriter, final Resource orderReportTemplate,
                         final DataService dataService) {

        this.jasperWriter = jasperWriter;
        this.orderReportTemplate = orderReportTemplate;
        this.dataService = dataService;
    }

    public File createOrderReport(final Order order) throws CannotGenerateReportException {
        Preconditions.checkNotNull(order, "Order cannot be null");
        log.info("Creating order report {}", order);

        final GeneralData generalData = dataService.getGeneralData()
                .orElseThrow(() -> new CannotGenerateReportException("Error generating report: missing general data!"));

        try {
            return jasperWriter.writeReport(orderReportTemplate.getFile(), constructOrderParameters(order, generalData));
        } catch (final JRException | IOException e) {
            throw new CannotGenerateReportException("Error generating report!", e);
        }
    }

    public File createOrdersReport(final List<Order> orders) throws CannotGenerateReportException {
        log.info("Creating jobs report for {} orders", orders.size());

       return null;
    }

    private Map<String, Object> constructOrderParameters(final Order order, final GeneralData generalData) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", generalData.getLaboratoryName());
        parameters.put("ADDRESS1", generalData.getAddress());
        parameters.put("ADDRESS2", String.format("%s, %s", generalData.getPostalCode(), generalData.getTown()));
        parameters.put("CONTACT", String.format("%s, %s",generalData.getPhone(), generalData.getEmail()));
        parameters.put("CLINIC", order.getClinic().getName());
        parameters.put("DOCTOR", order.getDoctor().toString());
        parameters.put("ORDER_NO", order.getId());
        parameters.put("COLOR", order.getToothColor().getName());
        parameters.put("DELIVERY_DATE", dateFormatter.format(order.getDeliveryDate()));
        parameters.put("JOBS", constructJobsParameter(order));
        parameters.put("ORDER_DATE", dateFormatter.format(order.getDate()));

        if (generalData.getLogo() != null) {
            parameters.put("LOGO", getLogoImage(generalData));
        }

        parameters.put("TEETH1", constructTeethParameter(order, 11, 18, true));
        parameters.put("TEETH2", constructTeethParameter(order, 21, 28, false));
        parameters.put("TEETH3", constructTeethParameter(order, 31, 38, false));
        parameters.put("TEETH4", constructTeethParameter(order, 41, 48, true));
        parameters.put("SAMPLES", constructSamplesParameter(order));
        parameters.put("EXECUTIONS", constructExecutionsParameter(order));

        return parameters;
    }

    private Image getLogoImage(final GeneralData generalData) {
        final StoredFile logoFile = generalData.getLogo();
        final InputStream in = new ByteArrayInputStream(logoFile.getContent());

        try {
            return ImageIO.read(in);
        } catch (final IOException e) {
            log.error("Error loading logo image: ", e);
            return null;
        }
    }

    private List<ToothDisplay> constructTeethParameter(final Order order, final int startNumber, final int endNumber,
                                                        boolean reversed) {

        final Map<Integer, Tooth> selectedTeeth = order.getJobs().stream()
                .map(Job::getTeeth)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Tooth::getNumber, t -> t));

        final Stream<Integer> stream = IntStream.range(startNumber, endNumber + 1).boxed();

        final Stream<Integer> sortedStream = reversed ? stream.sorted(Comparator.reverseOrder()) : stream;

        return sortedStream.map(num -> selectedTeeth.containsKey(num) ? selectedTeeth.get(num) : new Tooth(num))
                .map(ToothDisplay::new)
                .collect(Collectors.toList());
    }

    private List<SampleDisplay> constructSamplesParameter(final Order order) {
        return order.getJobs().stream()
                .map(Job::getSamples)
                .flatMap(Set::stream)
                .map(SampleDisplay::new)
                .collect(Collectors.toList());
    }

    private List<ExecutionDisplay> constructExecutionsParameter(final Order order) {
        return order.getJobs().stream()
                .map(Job::getExecutions)
                .flatMap(Set::stream)
                .map(ExecutionDisplay::new)
                .collect(Collectors.toList());
    }

    private List<JobTemplate> constructJobsParameter(final Order order) {
        return order.getJobs().stream()
                .map(Job::getTemplate)
                .collect(Collectors.toList());
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
