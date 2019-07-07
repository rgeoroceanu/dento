package com.company.dento.dao;

import com.company.dento.model.business.ExecutionTemplate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExecutionTemplateDao extends PageableRepository<ExecutionTemplate, Long> {

    @EntityGraph(attributePaths = {"individualPrices"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<ExecutionTemplate> findById(final Long id);
}
