package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.PurchaseEntity;
import com.ntr1x.treasure.web.model.purchase.ResourceType;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    
    PurchaseEntity findByResType(ResourceType type);
    List<PurchaseEntity> findAllByResType(ResourceType type);
    
    Page<PurchaseEntity> findByResTypeAndUserId(ResourceType type, long user, Pageable pageable);
    Page<PurchaseEntity> findByResTypeAndStatusAndUserId(ResourceType type, PurchaseEntity.Status status, long user, Pageable pageable);
}