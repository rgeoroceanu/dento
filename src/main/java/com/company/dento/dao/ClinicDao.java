package com.company.dento.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.Clinic;

@Repository
public interface ClinicDao extends JpaRepository<Clinic, Long> {

}
