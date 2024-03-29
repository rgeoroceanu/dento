package com.company.dento.service;

import com.company.dento.dao.specification.MaterialSpecification;
import com.company.dento.dao.specification.OrderSpecification;
import com.company.dento.model.business.*;
import com.company.dento.report.JasperWriter;
import com.company.dento.service.exception.CannotGenerateReportException;
import com.company.dento.service.exception.TooManyResultsException;
import com.company.dento.ui.localization.Localizer;
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
    private final Resource materialsReportTemplate;
    private final DataService dataService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ReportService(final JasperWriter jasperWriter, final Resource orderReportTemplate,
                         final Resource ordersReportTemplate, final Resource executionsReportTemplate,
                         final Resource materialsReportTemplate, final DataService dataService) {

        this.jasperWriter = jasperWriter;
        this.orderReportTemplate = orderReportTemplate;
        this.ordersReportTemplate = ordersReportTemplate;
        this.executionsReportTemplate = executionsReportTemplate;
        this.materialsReportTemplate = materialsReportTemplate;
        this.dataService = dataService;
    }

    public File createOrderReport(final Long orderId) throws CannotGenerateReportException {
        Preconditions.checkNotNull(orderId, "Order id cannot be null");
        log.info("Creating order report for order with id{}", orderId);

        final Order oder = dataService.getEntity(orderId, Order.class)
                .orElseThrow(() -> new CannotGenerateReportException("Error generating report: order id invalid!"));

        final GeneralData generalData = dataService.getGeneralData()
                .orElseThrow(() -> {
                    log.error("Error generating report: missing general data!");
                    return new CannotGenerateReportException("Error generating report: missing general data!");
                });

        try {
            return jasperWriter.writeReport(orderReportTemplate.getFile(), constructOrderParameters(oder, generalData));
        } catch (final JRException | IOException e) {
            log.error("Error generating report: {}", e.getMessage());
            throw new CannotGenerateReportException("Error generating report!", e);
        }
    }

    public File createOrdersReport(final OrderSpecification orderSpecification,
                                   final Map<String, Boolean> sortOrder) throws CannotGenerateReportException, TooManyResultsException {

        Preconditions.checkNotNull(orderSpecification, "Spec cannot be null");
        Preconditions.checkNotNull(sortOrder, "Sort order map cannot be null");
        log.info("Creating jobs report for order {} ", orderSpecification);

        if (dataService.countByCriteria(Order.class, orderSpecification) > 1000) {
            log.error("Error generating report: too many results");
            throw new TooManyResultsException("Too many entries for report generation!");
        }

        final GeneralData generalData = dataService.getGeneralData()
                .orElseThrow(() -> {
                    log.error("Error generating report: missing general data!");
                    return new CannotGenerateReportException("Error generating report: missing general data!");
                });

        final List<Order> orders = dataService.getByCriteria(Order.class, orderSpecification, 0, 1000, sortOrder);

        final Double totalPrice = dataService.getJobsPriceTotal(orderSpecification);;

        try {
            return jasperWriter.writeReport(ordersReportTemplate.getFile(), constructOrdersParameters(orders, generalData, totalPrice));
        } catch (final JRException | IOException e) {
            log.error("Error generating report: {}", e.getMessage());
            throw new CannotGenerateReportException("Error generating report!", e);
        }
    }

    public File createExecutionsReport(final OrderSpecification orderSpecification,
                                       final Map<String, Boolean> sortOrder) throws CannotGenerateReportException, TooManyResultsException {

        Preconditions.checkNotNull(orderSpecification, "Spec cannot be null");
        Preconditions.checkNotNull(sortOrder, "Sort order map cannot be null");
        log.info("Creating executions report for order {} ", orderSpecification);

        if (dataService.countByCriteria(Order.class, orderSpecification) > 1000) {
            log.error("Error generating report: too many results");
            throw new TooManyResultsException("Too many entries for report generation!");
        }

        final GeneralData generalData = dataService.getGeneralData()
                .orElseThrow(() -> {
                    log.error("Error generating report: missing general data!");
                    return new CannotGenerateReportException("Error generating report: missing general data!");
                });


        final List<Order> orders = dataService.getByCriteria(Order.class, orderSpecification, 0, 1000, sortOrder);

        final Double totalPrice = dataService.getExecutionsPriceTotal(orderSpecification);;

        try {
            return jasperWriter.writeReport(executionsReportTemplate.getFile(), constructExecutionsParameters(orders, generalData, totalPrice));
        } catch (final JRException | IOException e) {
            log.error("Error generating report: {}", e.getMessage());
            throw new CannotGenerateReportException("Error generating report!", e);
        }
    }

    public File createMaterialsReport(final MaterialSpecification materialSpecification,
                                      final Map<String, Boolean> sortOrder) throws CannotGenerateReportException, TooManyResultsException {

        Preconditions.checkNotNull(materialSpecification, "Spec cannot be null");
        Preconditions.checkNotNull(sortOrder, "Sort order map cannot be null");
        log.info("Creating materials report for spec {} ", materialSpecification);

        if (dataService.countByCriteria(Material.class, materialSpecification) > 1000) {
            log.error("Error generating report: too many results");
            throw new TooManyResultsException("Too many entries for report generation!");
        }

        final GeneralData generalData = dataService.getGeneralData()
                .orElseThrow(() -> {
                    log.error("Error generating report: missing general data!");
                    return new CannotGenerateReportException("Error generating report: missing general data!");
                });


        final List<Material> materials = dataService.getByCriteria(Material.class, materialSpecification, 0, 1000, sortOrder);

        final Double totalPrice = dataService.getMaterialPriceTotal(materialSpecification);

        try {
            return jasperWriter.writeReport(materialsReportTemplate.getFile(), constructMaterialsParameters(materials, generalData, totalPrice));
        } catch (final JRException | IOException e) {
            log.error("Error generating report: {}", e.getMessage());
            throw new CannotGenerateReportException("Error generating report!", e);
        }
    }

    private Map<String, Object> constructOrderParameters(final Order order, final GeneralData generalData) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", generalData.getLaboratoryName());
        parameters.put("ADDRESS1", generalData.getAddress());
        parameters.put("ADDRESS2", String.format("%s, %s", generalData.getPostalCode(), generalData.getTown()));
        parameters.put("CONTACT", String.format("%s, %s",generalData.getPhone(), generalData.getEmail()));
        parameters.put("CLINIC", order.getDoctor().getClinic().getName());
        parameters.put("DOCTOR", order.getDoctor().toString());
        parameters.put("ORDER_NO", order.getId());
        parameters.put("COLOR", order.getToothColor().getName());
        final String date = order.getDeliveryDate() != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy").format(order.getDeliveryDate()) : "";
        final String time = order.getDeliveryTime() != null ? DateTimeFormatter.ofPattern("HH:mm").format(order.getDeliveryTime()) : "";
        parameters.put("DELIVERY_DATE", String.format("%s %s", date, time));
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

    private Map<String, Object> constructOrdersParameters(final List<Order> orders, final GeneralData generalData,
                                                          final Double totalPrice) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", generalData.getLaboratoryName());
        parameters.put("ADDRESS1", generalData.getAddress());
        parameters.put("ADDRESS2", String.format("%s, %s", generalData.getPostalCode(), generalData.getTown()));
        parameters.put("CONTACT", String.format("%s, %s",generalData.getPhone(), generalData.getEmail()));
        parameters.put("DATE", dateFormatter.format(LocalDate.now()));
        parameters.put("TOTAL_PRICE", String.format(Localizer.getCurrentLocale(), "%.2f", totalPrice));

        if (generalData.getLogo() != null) {
            parameters.put("LOGO", getLogoImage(generalData));
        }

        parameters.put("ORDERS", constructOrdersParameter(orders));

        return parameters;
    }

    private Map<String, Object> constructExecutionsParameters(final List<Order> orders, final GeneralData generalData,
                                                              final Double totalPrice) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", generalData.getLaboratoryName());
        parameters.put("ADDRESS1", generalData.getAddress());
        parameters.put("ADDRESS2", String.format("%s, %s", generalData.getPostalCode(), generalData.getTown()));
        parameters.put("CONTACT", String.format("%s, %s",generalData.getPhone(), generalData.getEmail()));
        parameters.put("DATE", dateFormatter.format(LocalDate.now()));
        parameters.put("TOTAL_PRICE", String.format(Localizer.getCurrentLocale(), "%.2f", totalPrice));

        if (generalData.getLogo() != null) {
            parameters.put("LOGO", getLogoImage(generalData));
        }

        parameters.put("ORDERS", constructOrdersParameter(orders));

        return parameters;
    }

    private Map<String, Object> constructMaterialsParameters(final List<Material> materials, final GeneralData generalData,
                                                             final Double totalPrice) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_NAME", generalData.getLaboratoryName());
        parameters.put("ADDRESS1", generalData.getAddress());
        parameters.put("ADDRESS2", String.format("%s, %s", generalData.getPostalCode(), generalData.getTown()));
        parameters.put("CONTACT", String.format("%s, %s",generalData.getPhone(), generalData.getEmail()));
        parameters.put("DATE", dateFormatter.format(LocalDate.now()));
        parameters.put("TOTAL_PRICE", String.format(Localizer.getCurrentLocale(), "%.2f", totalPrice));

        if (generalData.getLogo() != null) {
            parameters.put("LOGO", getLogoImage(generalData));
        }

        parameters.put("MATERIALS", constructMaterialsParameter(materials));

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

        return sortedStream.map(num -> selectedTeeth.containsKey(num) ?
                new ToothDisplay(selectedTeeth.get(num), true) : new ToothDisplay(new Tooth(num), false))
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

    private List<MaterialDisplay> constructMaterialsParameter(final List<Material> materials) {
        return materials.stream()
                .map(MaterialDisplay::new)
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
