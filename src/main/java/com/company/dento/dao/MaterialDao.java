package com.company.dento.dao;

import com.company.dento.model.business.JobTemplate;
import com.company.dento.model.business.Material;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialDao extends PageableRepository<Material, Long> {
    @EntityGraph(attributePaths = {"individualPrices"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Material> findById(final Long id);
}
