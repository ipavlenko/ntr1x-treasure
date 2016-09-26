package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p1.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByEmail(String email);
}
