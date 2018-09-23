package com.company.dento.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.JobTemplate;

@Repository
public interface JobTemplateDao extends JpaRepository<JobTemplate, Long> {

}
