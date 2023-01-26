package com.company.dento.dao.specification;

import com.company.dento.model.business.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for advanced search of {@link Material} entities.
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Data
public class MaterialSpecification implements Specification<Material> {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private MaterialTemplate template;
    private Doctor doctor;
    private Clinic clinic;
    private JobTemplate job;

    @Override
    public Predicate toPredicate(final Root<Material> root, final CriteriaQuery<?> query, CriteriaBuilder builder) {

        final List<Predicate> predicates = new ArrayList<>();

        if (doctor != null) {
            predicates.add(builder.equal(root.get("job").get("order").get("doctor"), doctor));
        }

        if (template != null) {
            predicates.add(builder.equal(root.get("template"), template));
        }

        if (clinic != null) {
            predicates.add(builder.equal(root.get("job").get("order").get("doctor").get("clinic"), clinic));
        }

        if (job != null) {
            predicates.add(builder.equal(root.get("job"), job));
        }

        if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("job").get("order").get("date"), startDate));
        }

        if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("job").get("order").get("date"), endDate.toLocalDate().plusDays(1).atStartOfDay()));
        }

        final Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}