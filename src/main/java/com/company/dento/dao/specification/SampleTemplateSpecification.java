package com.company.dento.dao.specification;

import com.company.dento.model.business.SampleTemplate;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for advanced search of {@link SampleTemplate} entities.
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Setter
public class SampleTemplateSpecification implements Specification<SampleTemplate> {

    @Override
    public Predicate toPredicate(final Root<SampleTemplate> root,
                                 final CriteriaQuery<?> query, CriteriaBuilder builder) {

        final List<Predicate> predicates = new ArrayList<>();
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        predicates.add(builder.equal(root.get("deleted"), false));
        return builder.and(predicates.toArray(predicatesArray));
    }
}