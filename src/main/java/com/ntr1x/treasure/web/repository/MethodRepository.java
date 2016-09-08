package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Method;

public interface MethodRepository extends JpaRepository<Method, Long> {
    
    Method findByResType(Aspect type);
    List<Method> findAllByResType(Aspect type);
}
