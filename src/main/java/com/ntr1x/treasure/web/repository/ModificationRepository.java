package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.p4.Modification;

public interface ModificationRepository extends JpaRepository<Modification, Long> {
    
    @Query(
        " SELECT m"
      + " FROM Modification m"
      + " WHERE m.id IN :ids"
    )
    List<Modification> findByIdIn(@Param("ids") Long[] ids);
}
