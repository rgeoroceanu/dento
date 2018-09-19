package com.company.dento.dao;

import com.company.dento.model.business.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionDao extends JpaRepository<Execution, Long>, JpaSpecificationExecutor<Execution> {

}
