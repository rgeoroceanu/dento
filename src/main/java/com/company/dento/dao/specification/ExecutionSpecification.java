package com.company.dento.dao.specification;

import com.company.dento.model.business.Execution;
import com.company.dento.model.business.User;
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
public class ExecutionSpecification implements Specification<Execution> {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User technician;
    private Boolean finalized;
    private Long orderId;
    private String templateName;
    private String jobTemplateName;
    private Integer count;
    private Integer price;

    @Override
    public Predicate toPredicate(final Root<Execution> executionRoot,
                                 final CriteriaQuery<?> query, CriteriaBuilder builder) {

        final List<Predicate> predicates = new ArrayList<>();
        if (technician != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("technician"), technician)));
        }
        if (jobTemplateName != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("job").get("template").get("name"), jobTemplateName)));
        }
        if (templateName != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("template").get("name"), templateName)));
        }
        if (orderId != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("job").get("order").get("id"), orderId)));
        }
        if (finalized != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("job").get("order").get("finalized"), finalized)));
        }
        if (startDate != null) {
            predicates.add(builder.and(builder.greaterThanOrEqualTo(executionRoot.get("created"), startDate)));
        }
        if (endDate != null) {
            predicates.add(builder.and(builder.lessThanOrEqualTo(executionRoot.get("created"),
                    endDate.toLocalDate().plusDays(1).atStartOfDay())));
        }
        if (price != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("price"), price)));
        }
        if (count != null) {
            predicates.add(builder.and(builder.equal(executionRoot.get("count"), count)));
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}