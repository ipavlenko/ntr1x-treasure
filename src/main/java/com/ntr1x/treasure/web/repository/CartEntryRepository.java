package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.CartEntry;

public interface CartEntryRepository extends JpaRepository<CartEntry, Long> {
}
