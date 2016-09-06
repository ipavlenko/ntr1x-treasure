package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.security.SecurityResource;


public interface ResourceRepository extends JpaRepository<SecurityResource, Long> {
	
	SecurityResource findByAlias(String alias);
	Page<SecurityResource> findByAliasLikeOrderByAlias(String pattern, Pageable pageable);
	Page<SecurityResource> findOrderByAlias(Pageable pageable);
	
	SecurityResource findByResType(ResourceType type);
    List<SecurityResource> findAllByResType(ResourceType type);
}
