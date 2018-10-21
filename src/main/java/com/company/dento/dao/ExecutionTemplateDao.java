package com.company.dento.dao;

import com.company.dento.model.business.ExecutionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionTemplateDao extends PageableRepository<ExecutionTemplate, Long> {

}
