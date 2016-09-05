package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.assets.DeliveryPlace;
import com.ntr1x.treasure.web.model.purchase.ResourceType;

public interface DeliveryPlaceRepository extends JpaRepository<DeliveryPlace, Long> {
    
    DeliveryPlace findByResType(ResourceType type);
    List<DeliveryPlace> findAllByResType(ResourceType type);
}
