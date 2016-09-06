package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.GoodEntity;

public interface GoodRepository extends JpaRepository<GoodEntity, Long> {

    Page<GoodEntity> findByPurchaseId(long purchase, Pageable pageable);
}
