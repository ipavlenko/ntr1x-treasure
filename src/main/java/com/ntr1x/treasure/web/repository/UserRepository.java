package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.model.User.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query(
        " SELECT DISTINCT u"
      + " FROM"
      + "   User u"
      + " WHERE (:role IS NULL OR u.role = :role)"
    )
    Page<User> findUsersByRole(@Param("role") Role role, Pageable pageable);
    
    User findByEmail(String email);
}
