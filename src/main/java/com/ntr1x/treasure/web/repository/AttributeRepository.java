package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p1.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
