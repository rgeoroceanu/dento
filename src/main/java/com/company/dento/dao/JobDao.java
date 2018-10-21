package com.company.dento.dao;

import com.company.dento.model.business.Job;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobDao extends PageableRepository<Job, Long> {
	List<Job> findByOrderId(final Long orderId);
}
