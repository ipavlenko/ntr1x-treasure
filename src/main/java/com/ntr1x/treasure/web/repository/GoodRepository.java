package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p3.Good;

public interface GoodRepository extends JpaRepository<Good, Long> {

    Page<Good> findByPurchaseId(long purchase, Pageable pageable);
}
