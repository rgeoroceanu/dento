package com.company.dento.dao;

import com.company.dento.model.business.Execution;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionDao extends PageableRepository<Execution, Long> {

}
