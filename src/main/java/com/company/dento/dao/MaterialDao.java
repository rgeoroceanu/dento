package com.company.dento.dao;

import com.company.dento.model.business.Material;
import com.company.dento.model.business.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialDao extends PageableRepository<Material, Long> {

    @Query("SELECT COALESCE(SUM(m.quantity * m.price), 0) FROM Material m " +
            "WHERE (:startDate IS NULL OR m.job.order.date >= :startDate) " +
            "AND (:endDate IS NULL OR m.job.order.date <= :endDate) " +
            "AND (:templateId IS NULL OR m.template.id = :templateId) " +
            "AND (:doctorId IS NULL OR m.job.order.doctor.id = :doctorId) " +
            "AND (:jobId IS NULL OR m.job.template.id = :jobId) " +
            "AND (:clinicId IS NULL OR m.job.order.doctor.clinic.id = :clinicId)")
    double calculateMaterialsPriceTotal(final Long templateId, final Long jobId, final Long doctorId, final Long clinicId,
                                         final LocalDateTime startDate, final LocalDateTime endDate);
}
