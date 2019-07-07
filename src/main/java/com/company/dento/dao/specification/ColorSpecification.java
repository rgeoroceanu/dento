package com.company.dento.dao.specification;

import com.company.dento.model.business.Color;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification for advanced search of {@link Color} entities.
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Setter
public class ColorSpecification implements Specification<Color> {

    @Override
    public Predicate toPredicate(final Root<Color> root,
                                 final CriteriaQuery<?> query, CriteriaBuilder builder) {

        final List<Predicate> predicates = new ArrayList<>();
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}