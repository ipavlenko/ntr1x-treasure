package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.ResourceAttribute;

public interface ResourceAttributeRepository extends JpaRepository<ResourceAttribute, Long> {
}