package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p2.Depot;

public interface DepotRepository extends JpaRepository<Depot, Long> {
    
}
