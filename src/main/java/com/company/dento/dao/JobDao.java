package com.company.dento.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.Job;

@Repository
public interface JobDao extends JpaRepository<Job, Long> {
	List<Job> findByOrderId(final Long orderId);
}
