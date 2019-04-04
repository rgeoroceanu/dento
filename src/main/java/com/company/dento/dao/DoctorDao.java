package com.company.dento.dao;

import com.company.dento.model.business.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDao extends PageableRepository<Doctor, Long> {
}
