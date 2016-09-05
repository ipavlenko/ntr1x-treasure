package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.GoodEntity;
import com.ntr1x.treasure.web.model.purchase.ResourceType;

public interface GoodEntityRepository extends JpaRepository<GoodEntity, Long> {
    
    GoodEntity findByResType(ResourceType type);
    List<GoodEntity> findAllByResType(ResourceType type);
}
