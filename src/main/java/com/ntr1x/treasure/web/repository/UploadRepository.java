package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.assets.Upload;

public interface UploadRepository extends JpaRepository<UploadRepository, Long> {
    
    Upload findByUuid(String uuid);
}
