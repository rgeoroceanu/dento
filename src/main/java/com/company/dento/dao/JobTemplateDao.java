package com.company.dento.dao;

import com.company.dento.model.business.JobTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTemplateDao extends PageableRepository<JobTemplate, Long> {

}
