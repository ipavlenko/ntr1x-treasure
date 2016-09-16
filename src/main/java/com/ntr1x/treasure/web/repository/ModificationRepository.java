package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.p4.Modification;

public interface ModificationRepository extends JpaRepository<Modification, Long> {
}
