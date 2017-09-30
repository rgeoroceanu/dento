package com.company.dento.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.Execution;

@Repository
public interface ExecutionDao extends JpaRepository<Execution, Long> {
	List<Execution> findByProcedureId(Long procedureId);
}
