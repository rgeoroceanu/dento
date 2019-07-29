package com.company.dento.model.business;

import com.company.dento.ui.localization.Localizer;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDisplay {

    private String date;
    private String deliveryDate;
    private Long id;
    private String pacient;
    private String doctor;
    private String clinic;
    private String finalized;
    private String paid;
    private String price;
    private List<JobDisplay> jobs;

    public OrderDisplay(final Order order) {
        this.date = order.getDate() != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy").format(order.getDate()) : "";
        this.id = order.getId();
        this.doctor = order.getDoctor() != null ? String.format("%s %s",
                order.getDoctor().getFirstName(), order.getDoctor().getLastName()) : "";

        this.clinic = order.getDoctor() != null ? order.getDoctor().getClinic().getName() : "";
        this.pacient = order.getPatient();
        this.finalized = order.isFinalized() ? "Da" : "Nu";
        this.paid = order.isPaid() ? "Da" : "Nu";
        this.price = String.format(Localizer.getCurrentLocale(), "%.2f", order.getTotalPrice());
        this.jobs = order.getJobs().stream().map(JobDisplay::new).collect(Collectors.toList());
        final String date = order.getDeliveryDate() != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy").format(order.getDeliveryDate()) : "";
        final String time = order.getDeliveryTime() != null ? DateTimeFormatter.ofPattern("HH:mm").format(order.getDeliveryTime()) : "";
        this.deliveryDate = String.format("%s %s", date, time);
    }
}
