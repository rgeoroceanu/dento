package com.company.dento.dao;

import com.company.dento.model.business.Clinic;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicDao extends PageableRepository<Clinic, Long> {

}
