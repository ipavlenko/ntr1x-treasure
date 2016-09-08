package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Depot;

public interface DepotRepository extends JpaRepository<Depot, Long> {
    
    Depot findByResType(Aspect type);
    List<Depot> findAllByResType(Aspect type);
}
