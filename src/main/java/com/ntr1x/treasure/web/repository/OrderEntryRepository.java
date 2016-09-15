package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p5.OrderEntry;

public interface OrderEntryRepository extends JpaRepository<OrderEntry, Long> {
    
//    List<OrderEntry> getByPurchaseId(@Param("purchase") Long purchase);
}
