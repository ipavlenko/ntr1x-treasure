package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.CartEntry;

public interface CartEntryRepository extends JpaRepository<CartEntry, Long> {
    
    List<CartEntry> getByCartIdAndModificationGoodPurchaseId(@Param("cart") Long cart, @Param("purchase") Long purchase);
}

