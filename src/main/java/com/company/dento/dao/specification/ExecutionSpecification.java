package com.company.dento.dao.specification;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionCriteria;
import com.google.common.base.Preconditions;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for advanced search of {@link Execution} entities.
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class ExecutionSpecification implements Specification<Execution> {

    private final ExecutionCriteria executionCriteria;

    public ExecutionSpecification(final ExecutionCriteria executionCriteria) {
        this.executionCriteria = executionCriteria;
    }

    @Override
    public Predicate toPredicate(final Root<Execution> executionRoot,
                                 final CriteriaQuery<?> query, CriteriaBuilder builder) {

        Preconditions.checkNotNull(executionCriteria, "Search criteria can not be null!");

        final List<Predicate> predicates = new ArrayList<>();
        if (executionCriteria.getTechnician() != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("job").get("technician"),
                    executionCriteria.getTechnician())));
        }

        predicates.add(builder.and(builder.equal(executionRoot.get("job").get("finalized"),
                executionCriteria.isFinalized())));

        if (executionCriteria.getStartDate() != null) {
            predicates.add(builder.and(builder.greaterThanOrEqualTo(executionRoot.get("created"),
                    executionCriteria.getStartDate())));
        }
        if (executionCriteria.getEndDate() != null) {
            predicates.add(builder.and(builder.lessThanOrEqualTo(executionRoot.get("created"),
                    executionCriteria.getEndDate())));
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}