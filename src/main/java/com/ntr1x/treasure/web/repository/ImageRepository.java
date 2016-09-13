package com.ntr1x.treasure.web.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(
        " SELECT DISTINCT i"
      + " FROM"
      + "   Image i"
      + "   JOIN i.aspects a"
      + " WHERE (:aspect IS NULL OR a = :aspect)"
    )
    Page<Image> findByAspect(
        @Param("aspect") String aspect,
        Pageable pageable
    );
    
    Image findByUuid(UUID uuid);
}
