package com.company.dento.dao;

import com.company.dento.model.business.Color;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorDao extends PageableRepository<Color, Long> {

}
