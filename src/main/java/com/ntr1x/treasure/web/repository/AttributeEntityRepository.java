package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.attributes.AttributeEntity;

public interface AttributeEntityRepository extends JpaRepository<AttributeEntity, Long> {
}
