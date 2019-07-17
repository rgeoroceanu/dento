package com.company.dento.dao;

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
public interface OrderDao extends PageableRepository<Order, Long> {
    @EntityGraph(attributePaths = {"jobs", "jobs.teeth", "storedFiles", "jobs.samples", "jobs.executions", "jobs.materials", "jobs.template.individualPrices"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findById(Long id);

    @EntityGraph(value = "order.jobs", attributePaths = {"jobs", "jobs.executions"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAll(Specification<Order> spec, int offset, int limit, Sort sort);

    @Query("SELECT SUM(e.count * e.price) FROM Execution e " +
            "WHERE e.job.id IN (SELECT j.id FROM Job j " +
            "WHERE j.order.id IN (SELECT o.id FROM Order o " +
            "WHERE (:startDate IS NULL OR o.date >= :startDate) " +
            "AND (:endDate IS NULL OR o.date <= :endDate) " +
            "AND (:finalized IS NULL OR o.finalized = :finalized))) " +
            "AND :technicianId IS NULL OR e.technician.id = :technicianId")
    double calculateExecutionsPriceTotal(final Long technicianId, final Boolean finalized,
                                         final LocalDateTime startDate, final LocalDateTime endDate);

    @Query("SELECT SUM(j.count * j.price) FROM Job j " +
            "WHERE j.order.id IN (SELECT o.id FROM Order o " +
            "WHERE (:id IS NULL OR o.id = :id) " +
            "AND (:startDate IS NULL OR o.date >= :startDate) " +
            "AND (:endDate IS NULL OR o.date <= :endDate) " +
            "AND (:finalized IS NULL OR o.finalized = :finalized) " +
            "AND (:paid IS NULL OR o.paid = :paid) " +
            "AND (:patient IS NULL OR :patient <> '' OR o.patient LIKE CONCAT('%', :patient, '%') " +
            "AND (:doctorId IS NULL OR o.doctor.id = :doctorId) " +
            "AND (:clinicId IS NULL OR o.doctor.clinic.id = :clinicId)))")
    double calculateJobsPriceTotal(final Long id, final LocalDateTime startDate, final LocalDateTime endDate,
                                   final String patient, final Long doctorId, final Long clinicId,
                                   final Boolean finalized, final Boolean paid);
}
