package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p0.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
	
	Resource findByAlias(String alias);
	Page<Resource> findByAliasLikeOrderByAlias(String pattern, Pageable pageable);
	Page<Resource> findOrderByAlias(Pageable pageable);
}
