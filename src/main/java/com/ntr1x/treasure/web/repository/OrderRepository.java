package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p3.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
        " SELECT o"
      + " FROM Order o"
      + " INNER JOIN o.purchase p"
      + " INNER JOIN o.user u"
      + " WHERE (:purchase IS NULL OR p.id = :purchase)"
      + "   AND (:user IS NULL OR u.id = :user)"
      + "   AND (:status IS NULL OR o.status = :status)"
    )
    Page<Order> findByStatusAndUserIdAndPurchaseId(
        @Param("status") Order.Status status,
        @Param("user") Long user,
        @Param("purchase") Long purchase,
        Pageable pageable
    );
}
