package com.company.dento.dao;

import com.company.dento.model.business.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao extends PageableRepository<Order, Long> {
    @EntityGraph(value = "order.jobs", attributePaths = {"jobs", "jobs.samples", "jobs.executions"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findById(Long id);

    @EntityGraph(value = "order.jobs", attributePaths = {"jobs"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAll(Specification<Order> spec, int offset, int limit, Sort sort);
}
