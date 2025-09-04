package com.tech.s3test.adapter.storage;

import com.tech.s3test.dto.res.FileResDto;
import org.springframework.web.multipart.MultipartFile;

public interface StorageAdapter {
    void save(String key, MultipartFile file);
    FileResDto get(String key);
    void update(String key, MultipartFile file);
    void delete(String key);
}
