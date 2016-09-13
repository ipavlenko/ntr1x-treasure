package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.model.User.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Page<User> findUsersByRole(Role role, Pageable pageable);
    
    User findByEmail(String email);
}
