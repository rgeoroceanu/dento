package com.company.dento.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

}
