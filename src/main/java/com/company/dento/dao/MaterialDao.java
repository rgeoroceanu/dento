package com.company.dento.dao;

import com.company.dento.model.business.Material;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialDao extends PageableRepository<Material, Long> {

}
