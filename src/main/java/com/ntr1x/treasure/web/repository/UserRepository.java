package com.ntr1x.treasure.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.security.SecurityUser;
import com.ntr1x.treasure.web.model.security.SecurityUser.Role;

public interface UserRepository extends JpaRepository<SecurityUser, Long> {
    
    Page<SecurityUser> findUsersByRole(Role role, Pageable pageable);
    
    SecurityUser findByResType(ResourceType type);
    SecurityUser findByEmail(String email);
}
