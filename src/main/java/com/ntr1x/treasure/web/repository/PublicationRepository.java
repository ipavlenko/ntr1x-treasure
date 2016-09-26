package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p3.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	
    @Query(
          " SELECT p"
        + " FROM Publication p"
        + " WHERE p.id IN :ids"
    )
    List<Publication> findByIdIn(@Param("ids") Long[] ids);
}
