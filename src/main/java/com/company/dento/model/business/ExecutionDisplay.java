package com.company.dento.model.business;

import lombok.Data;

@Data
public class ExecutionDisplay {

    private String name;
    private String job;
    private String technician;

    public ExecutionDisplay(final Execution execution) {
        this.name = execution.getTemplate().getName();
        this.job = execution.getJob().getTemplate().getName();
        final User technician = execution.getTechnician();
        this.technician = String.format("%s %s", technician.getFirstName(), technician.getLastName());
    }
}
