package com.company.dento.dao;

import com.company.dento.model.business.ToothColor;
import org.springframework.stereotype.Repository;

@Repository
public interface ToothColorDao extends PageableRepository<ToothColor, Long> {

}
