package com.company.dento.dao.specification;

import com.company.dento.model.business.Order;
import com.company.dento.model.business.*;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for advanced search of {@link Execution} entities.
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Data
public class OrderSpecification implements Specification<Order> {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String patient;
    private Doctor doctor;
    private Clinic clinic;
    private Boolean finalized;
    private Boolean paid;
    private User technician;
    private Boolean withExecutions;

    @Override
    public Predicate toPredicate(final Root<Order> root, final CriteriaQuery<?> query, CriteriaBuilder builder) {

        final List<Predicate> predicates = new ArrayList<>();

        if (doctor != null) {
            predicates.add(builder.equal(root.get("doctor"), doctor));
        }

        if (StringUtils.isNotEmpty(patient)) {
            predicates.add(builder.like(builder.lower(root.get("patient")), "%" + patient.toLowerCase() + "%"));
        }

        if (clinic != null) {
            predicates.add(builder.equal(root.get("doctor").get("clinic"), clinic));
        }

        if (id != null) {
            predicates.add(builder.equal(root.get("id"), id));
        }

        if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("created"), startDate));
        }

        if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("created"), endDate.toLocalDate().plusDays(1).atStartOfDay()));
        }

        if (finalized != null) {
            predicates.add(builder.equal(root.get("finalized"), finalized));
        }

        if (paid != null) {
            predicates.add(builder.equal(root.get("paid"), paid));
        }

        final Join<Order, Job> jobs = root.join("jobs");

        if (technician != null) {
            final Join<Order, Execution> executions = jobs.join("executions");
            predicates.add(builder.equal(executions.get("technician"), technician));
        }

        if (withExecutions != null && withExecutions) {
            predicates.add(builder.and(
                    builder.greaterThan(builder.size(root.get("jobs")), 0),
                    builder.greaterThan(builder.size(jobs.get("executions")), 0)));
        }

        query.distinct(true);

        final Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}