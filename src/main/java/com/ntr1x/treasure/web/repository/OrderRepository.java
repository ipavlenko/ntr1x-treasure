package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
          " SELECT DISTINCT o"
        + " FROM OrderEntity o"
        + " INNER JOIN o.entries e "
        + " INNER JOIN e.good.purchase p"
        + " WHERE p.id = :purchase"
    )
    List<Order> findByPurchaseId(@Param("purchase") Long purchase);
}
