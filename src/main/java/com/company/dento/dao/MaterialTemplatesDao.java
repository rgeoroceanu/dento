package com.company.dento.dao;

import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.MaterialTemplate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialTemplatesDao extends PageableRepository<MaterialTemplate, Long> {

    Optional<MaterialTemplate> findById(final Long id);
    List<MaterialTemplate> findAll();
}
