package com.company.dento.model.business;

import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class SampleDisplay {

    private String name;
    private String job;
    private String date;
    private String time;

    public SampleDisplay(final Sample sample) {
        this.name = sample.getTemplate().getName();
        this.job = sample.getJob().getTemplate().getName();
        this.date = sample.getDate() != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy").format(sample.getDate()) : "";
        this.time = sample.getTime() != null ? DateTimeFormatter.ofPattern("HH:mm").format(sample.getTime()) : "";
    }
}
