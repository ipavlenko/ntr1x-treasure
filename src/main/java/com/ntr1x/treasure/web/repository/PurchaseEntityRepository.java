package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.PurchaseEntity;
import com.ntr1x.treasure.web.model.purchase.ResourceType;

public interface PurchaseEntityRepository extends JpaRepository<PurchaseEntity, Long> {
    
    PurchaseEntity findByResType(ResourceType type);
    List<PurchaseEntity> findAllByResType(ResourceType type);
}