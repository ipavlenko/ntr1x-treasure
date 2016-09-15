package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p2.ResourceLike;

public interface LikeRepository extends JpaRepository<ResourceLike, Long> {
    
    ResourceLike findByRelateIdAndUserId(@Param("relate") long relate, @Param("user") long user);
}
