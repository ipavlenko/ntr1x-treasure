package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p2.Method;

public interface MethodRepository extends JpaRepository<Method, Long> {
    
}
