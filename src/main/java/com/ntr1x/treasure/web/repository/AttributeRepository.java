package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p1.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query(
        " SELECT DISTINCT a"
      + " FROM"
      + "   Attribute a"
      + "   JOIN a.aspects aspect"
      + " WHERE (aspect IN :aspects)"
    )
    Page<Attribute> findByAspects(
        @Param("aspects") List<String> aspects,
        Pageable pageable
    );
}
