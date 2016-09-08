package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    Attachment findByUuid(String uuid);
}
