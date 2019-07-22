package com.company.dento.dao;

import com.company.dento.model.business.MaterialTemplate;
import com.company.dento.model.business.ToothOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToothOptionDao extends PageableRepository<ToothOption, Long> {

    @EntityGraph(attributePaths = {"specificJob.individualPrices", "specificJob.sampleTemplates", "specificJob.executionTemplates", "specificJob.materials"}, type = EntityGraph.EntityGraphType.LOAD)
    List<ToothOption> findAll();
}
