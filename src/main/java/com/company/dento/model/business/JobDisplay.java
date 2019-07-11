package com.company.dento.model.business;

import com.company.dento.ui.localization.Localizer;
import lombok.Data;

@Data
public class JobDisplay {

    private String name;
    private String pricePerElement;
    private String priceTotal;
    private Integer count;

    public JobDisplay(final Job job) {
        this.name = job.getTemplate().getName();
        this.pricePerElement = String.format(Localizer.getCurrentLocale(), "%.2f", job.getPrice());
        this.priceTotal = String.format(Localizer.getCurrentLocale(), "%.2f", job.getPrice() * job.getCount());
        this.count = job.getCount();
    }
}
