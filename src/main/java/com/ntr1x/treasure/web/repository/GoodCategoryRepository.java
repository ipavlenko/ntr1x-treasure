package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.GoodCategory;

public interface GoodCategoryRepository extends JpaRepository<GoodCategory, Long> {

}
