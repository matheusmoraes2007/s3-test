package com.tech.s3test.service;

import com.tech.s3test.adapter.storage.StoragePort;
import com.tech.s3test.configuration.log.Log;
import com.tech.s3test.dto.res.FileResDto;
import com.tech.s3test.dto.res.SaveFileResDto;
import com.tech.s3test.exception.custom.MissingStatementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final StoragePort storagePort;

    @Log
    public SaveFileResDto save(MultipartFile file) {
        String key = this.getKey(file);
        storagePort.save(key, file);
        return new SaveFileResDto(key);
    }

    @Log
    public FileResDto downloadByKey(String key) {
        return storagePort.get(key);
    }

    @Log
    public void putUpdateByKey(String key, MultipartFile file) {
        storagePort.update(key, file);
    }

    @Log
    public void deleteByKey(String key) {
        storagePort.delete(key);
    }

    private String getKey(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new MissingStatementException("File name is empty");
        }
        if (!fileName.contains(".")) {
            throw new MissingStatementException("File extension is empty");
        }
        String fileExtension = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
        return UUID.randomUUID() + fileExtension;
    }
}
