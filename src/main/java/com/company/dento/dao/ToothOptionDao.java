package com.company.dento.dao;

import com.company.dento.model.business.ToothOption;
import org.springframework.stereotype.Repository;

@Repository
public interface ToothOptionDao extends PageableRepository<ToothOption, Long> {

}
