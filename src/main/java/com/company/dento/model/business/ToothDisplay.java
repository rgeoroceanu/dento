package com.company.dento.model.business;

import lombok.Data;

@Data
public class ToothDisplay {
    private String toothNumber;
    private String toothType;
    private String toothProperty;


    public ToothDisplay(final Tooth tooth) {
        this.toothNumber = String.valueOf(tooth.getNumber());
        this.toothType = tooth.getType() != null ? tooth.getType().name() : "";
        this.toothProperty = tooth.getProperty() != null ? tooth.getProperty().name() : "";
    }
}
