package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    Purchase findByResType(Aspect type);
    List<Purchase> findAllByResType(Aspect type);
    
    Page<Purchase> findByResTypeAndUserId(Aspect type, Long user, Pageable pageable);
    Page<Purchase> findByResTypeAndStatusAndUserId(Aspect type, Purchase.Status status, Long user, Pageable pageable);
}