package com.tech.s3test.repository;

import com.tech.s3test.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {
    boolean existsByKeyAndUserId(String key, Long userId);

    void deleteByKey(String key);
}
