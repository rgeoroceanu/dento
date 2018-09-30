package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ExecutionCriteria {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User technician;
    private boolean finalized;
    private Long orderId;
    private String templateName;
    private String jobTemplateName;
    private Integer count;
    private Integer price;

}