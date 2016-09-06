package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.CartEntryEntity;

public interface CartEntryEntityRepository extends JpaRepository<CartEntryEntity, Long> {
}