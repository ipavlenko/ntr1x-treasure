package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    Page<Purchase> findByUserId(Long user, Pageable pageable);
    Page<Purchase> findByUserIdAndStatus(Long user, Purchase.Status status, Pageable pageable);
}