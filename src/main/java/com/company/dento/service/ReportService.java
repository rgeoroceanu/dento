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
    private final Resource ordersReportTemplate;
    private final Resource executionsReportTemplate;
    private final DataService dataService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ReportService(final JasperWriter jasperWriter, final Resource orderReportTemplate,
                         final Resource ordersReportTemplate, final Resource executionsReportTemplate,
                         final DataService dataService) {

        this.jasperWriter = jasperWriter;
        this.orderReportTemplate = orderReportTemplate;
        this.ordersReportTemplate = ordersReportTemplate;
        this.executionsReportTemplate = executionsReportTemplate;
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

        final GeneralData generalData = dataService.getGeneralData()
                .orElseThrow(() -> new CannotGenerateReportException("Error generating report: missing general data!"));

        try {
            return jasperWriter.writeReport(ordersReportTemplate.getFile(), constructOrdersParameters(orders, generalData));
        } catch (final JRException | IOException e) {
            throw new CannotGenerateReportException("Error generating report!", e);
        }
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

    private Map<String, Object> constructOrdersParameters(final List<Order> orders, final GeneralData generalData) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", generalData.getLaboratoryName());
        parameters.put("ADDRESS1", generalData.getAddress());
        parameters.put("ADDRESS2", String.format("%s, %s", generalData.getPostalCode(), generalData.getTown()));
        parameters.put("CONTACT", String.format("%s, %s",generalData.getPhone(), generalData.getEmail()));
        parameters.put("DATE", dateFormatter.format(LocalDate.now()));

        if (generalData.getLogo() != null) {
            parameters.put("LOGO", getLogoImage(generalData));
        }

        parameters.put("ORDERS", constructOrdersParameter(orders));

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

    private List<OrderDisplay> constructOrdersParameter(final List<Order> orders) {
        return orders.stream()
                .map(OrderDisplay::new)
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
}
