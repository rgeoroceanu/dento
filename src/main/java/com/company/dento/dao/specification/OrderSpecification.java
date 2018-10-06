package com.company.dento.dao.specification;

import com.company.dento.model.business.Clinic;
import com.company.dento.model.business.Doctor;
import com.company.dento.model.business.Execution;
import com.company.dento.model.business.Order;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for advanced search of {@link Execution} entities.
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Setter
public class OrderSpecification implements Specification<Order> {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String patient;
    private Doctor doctor;
    private Clinic clinic;
    private Integer price;
    private Boolean finalized;
    private Boolean paid;

    @Override
    public Predicate toPredicate(final Root<Order> root, final CriteriaQuery<?> query, CriteriaBuilder builder) {

        final List<Predicate> predicates = new ArrayList<>();
        if (doctor != null) {
            predicates.add(builder.and(builder.equal(root.get("doctor"), doctor)));
        }
        if (patient != null) {
            predicates.add(builder.and(builder.like(builder.lower(root.get("patient")), "%" + patient.toLowerCase() + "%")));
        }
        if (clinic != null) {
            predicates.add(builder.and(builder.equal(root.get("clinic"), clinic)));
        }
        if (id != null) {
            predicates.add(builder.and(builder.equal(root.get("id"), id)));
        }
        if (startDate != null) {
            predicates.add(builder.and(builder.greaterThanOrEqualTo(root.get("created"), startDate)));
        }
        if (endDate != null) {
            predicates.add(builder.and(builder.lessThanOrEqualTo(root.get("created"), endDate.toLocalDate().plusDays(1).atStartOfDay())));
        }
        if (price != null) {
            predicates.add(builder.and(builder.equal(root.get("price"), price)));
        }
        if (finalized != null) {
            predicates.add(builder.and(builder.equal(root.get("finalized"), finalized)));
        }
        if (paid != null) {
            predicates.add(builder.and(builder.equal(root.get("paid"), paid)));
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}