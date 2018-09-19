package com.company.dento.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.Sample;

@Repository
public interface SampleDao extends JpaRepository<Sample, Long> {
	List<Sample> findByOrderId(final Long orderId);
}
