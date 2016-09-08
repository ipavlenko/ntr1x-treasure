package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Good;

public interface GoodRepository extends JpaRepository<Good, Long> {

    Good findByResType(Aspect type);
    List<Good> findAllByResType(Aspect type);
    
    Page<Good> findByPurchaseId(long purchase, Pageable pageable);
}
