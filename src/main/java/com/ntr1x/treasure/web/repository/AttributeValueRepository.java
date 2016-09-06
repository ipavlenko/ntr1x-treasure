package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.attributes.AttributeValue;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
}
