package com.company.dento.dao;

import com.company.dento.model.business.SampleTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleTemplateDao extends PageableRepository<SampleTemplate, Long> {

}
