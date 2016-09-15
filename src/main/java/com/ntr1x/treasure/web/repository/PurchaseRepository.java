package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p2.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
       
    @Query(
        " SELECT p"
      + " FROM Purchase p"
      + " INNER JOIN p.user u"
      + " WHERE (:user IS NULL OR u.id = :user)"
      + "   AND (:status IS NULL OR p.status = :status)"
    )
    Page<Purchase> findByUserIdAndStatus(
        @Param("user") Long user,
        @Param("status") Purchase.Status status,
        Pageable pageable
    );
}