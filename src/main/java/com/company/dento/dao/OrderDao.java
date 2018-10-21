package com.company.dento.dao;

import com.company.dento.model.business.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao extends PageableRepository<Order, Long> {

}
