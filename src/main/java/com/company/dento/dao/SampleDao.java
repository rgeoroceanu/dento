package com.company.dento.dao;

import com.company.dento.model.business.Sample;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleDao extends PageableRepository<Sample, Long> {
	List<Sample> findByJobOrderId(final Long orderId);
}
