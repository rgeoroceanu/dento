package com.company.dento.model.business;

import lombok.Data;

@Data
public class ToothDisplay {
    private String toothNumber;
    private String toothType;
    private String toothProperty;


    public ToothDisplay(final Tooth tooth) {
        this.toothNumber = String.valueOf(tooth.getNumber());
        this.toothType = tooth.getOption1() != null ? tooth.getOption1().getName() : "";
        this.toothProperty = tooth.getOption2() != null ? tooth.getOption2().getName() : "";
    }
}
