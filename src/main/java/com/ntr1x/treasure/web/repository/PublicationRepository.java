package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	
//	@Query("SELECT p FROM Publication p WHERE p.firstname = :firstname or u.lastname = :lastname")
//	List<PublicationRepository> findByTag(@Param("tag") String tag);
}
