package com.tech.s3test.service;

import com.tech.s3test.adapter.storage.StoragePort;
import com.tech.s3test.dto.res.FileResDto;
import com.tech.s3test.dto.res.SaveFileResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final StoragePort storagePort;

    public SaveFileResDto save(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        this.validateFileName(originalFileName);
        String fileExtension = originalFileName.substring(file.getOriginalFilename().lastIndexOf("."));
        String key = UUID.randomUUID() + fileExtension;
        storagePort.save(key, file);
        return new SaveFileResDto(key);
    }

    public FileResDto downloadByKey(String key) {
        return storagePort.get(key);
    }

    public void putUpdateByKey(String key, MultipartFile file) {
        this.validateFileName(key);
        storagePort.update(key, file);
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("File name is empty");
        }
        if (!fileName.contains(".")) {
            throw new RuntimeException("File extension is empty");
        }
    }
}
