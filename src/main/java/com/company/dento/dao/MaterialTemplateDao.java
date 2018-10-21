package com.company.dento.dao;

import com.company.dento.model.business.MaterialTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialTemplateDao extends PageableRepository<MaterialTemplate, Long> {

}
