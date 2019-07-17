package com.company.dento.model.business;

import com.company.dento.ui.localization.Localizer;
import lombok.Data;

@Data
public class MaterialDisplay {

    private String name;
    private String price;
    private String priceTotal;
    private String quantity;

    public MaterialDisplay(final Material material) {
        this.name = material.getTemplate().getName();
        this.price = String.format(Localizer.getCurrentLocale(), "%.2f %s", material.getPrice(), material.getTemplate().getMeasurementUnit());
        this.priceTotal = String.format(Localizer.getCurrentLocale(), "%.2f", material.getPrice() * material.getQuantity());
        this.quantity = String.format(Localizer.getCurrentLocale(), "%.2f", material.getQuantity());
    }
}
