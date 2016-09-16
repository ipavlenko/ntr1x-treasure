package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p3.Good;

public interface GoodRepository extends JpaRepository<Good, Long> {

    @Query(
        " SELECT g"
      + " FROM Good g"
      + "   JOIN g.purchase p"
      + " WHERE (:purchase IS NULL OR p.id = :purchase)"
    )
    Page<Good> findByPurchaseId(@Param("purchase") Long purchase, Pageable pageable);
}
