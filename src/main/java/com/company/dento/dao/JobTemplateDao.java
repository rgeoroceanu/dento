package com.company.dento.dao;

import com.company.dento.model.business.JobTemplate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobTemplateDao extends PageableRepository<JobTemplate, Long> {
    @EntityGraph(attributePaths = {"sampleTemplates", "executionTemplates", "materials", "individualPrices"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<JobTemplate> findById(final Long id);

    @EntityGraph(attributePaths = {"sampleTemplates", "executionTemplates", "individualPrices", "materials",
            "executionTemplates.individualPrices"}, type = EntityGraph.EntityGraphType.LOAD)
    List<JobTemplate> findAll();
}
