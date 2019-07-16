package com.company.dento.model.business;

import com.company.dento.ui.localization.Localizer;
import lombok.Data;

@Data
public class ExecutionDisplay {

    private String name;
    private String job;
    private String technician;
    private String price;
    private int count;
    private String priceTotal;

    public ExecutionDisplay(final Execution execution) {
        this.name = execution.getTemplate().getName();
        this.job = execution.getJob().getTemplate().getName();
        final User technician = execution.getTechnician();
        this.technician = String.format("%s %s", technician != null ? technician.getFirstName() : "",
                technician != null ? technician.getLastName() : "");
        this.price = String.format(Localizer.getCurrentLocale(), "%.2f", execution.getPrice());
        this.count = execution.getCount();
        this.priceTotal = String.format(Localizer.getCurrentLocale(), "%.2f", execution.getPrice() * execution.getCount());
    }
}
