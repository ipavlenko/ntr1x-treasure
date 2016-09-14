package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.Grant;

public interface GrantRepository extends JpaRepository<Grant, Long> {
    
    @Query(
        " SELECT COUNT(g.id)"
      + " FROM"
      + "     Grant g"
      + " WHERE g.action = :action"
      + "   AND g.user.id = :user"
      + "   AND LOCATE(g.pattern, :resource) = 1"
    )
    int check(
        @Param("user") long user,
        @Param("resource") String resource,
        @Param("action") String action
    );
}
