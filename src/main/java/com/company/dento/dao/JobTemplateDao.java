package com.company.dento.dao;

import com.company.dento.model.business.JobTemplate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobTemplateDao extends PageableRepository<JobTemplate, Long> {
    @EntityGraph(value = "order.samples", attributePaths = {"sampleTemplates"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<JobTemplate> findById(Long id);
}
