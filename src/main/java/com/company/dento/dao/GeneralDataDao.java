package com.company.dento.dao;

import com.company.dento.model.business.GeneralData;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneralDataDao extends PageableRepository<GeneralData, Long> {
}
