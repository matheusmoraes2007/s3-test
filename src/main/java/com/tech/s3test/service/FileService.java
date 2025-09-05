package com.tech.s3test.service;

import com.tech.s3test.adapter.storage.StoragePort;
import com.tech.s3test.dto.res.FileResDto;
import com.tech.s3test.dto.res.SaveFileResDto;
import com.tech.s3test.exception.custom.MissingStatementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final StoragePort storagePort;

    public SaveFileResDto save(MultipartFile file) {
        log.info("Starting save process");
        String originalFileName = file.getOriginalFilename();
        this.validateFileName(originalFileName);
        String fileExtension = originalFileName.substring(file.getOriginalFilename().lastIndexOf("."));
        String key = UUID.randomUUID() + fileExtension;
        log.debug("Unique key generated: {}", key);
        storagePort.save(key, file);
        log.info("File has been saved successfully, key: {}", key);
        return new SaveFileResDto(key);
    }

    public FileResDto downloadByKey(String key) {
        return storagePort.get(key);
    }

    public void putUpdateByKey(String key, MultipartFile file) {
        log.info("Starting update process, key: {}", key);
        this.validateFileName(key);
        storagePort.update(key, file);
        log.info("File has been updated successfully, key: {}", key);
    }

    public void deleteByKey(String key) {
        log.info("Starting delete process, key: {}", key);
        this.validateFileName(key);
        storagePort.delete(key);
        log.info("File has been deleted successfully, key: {}", key);
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new MissingStatementException("File name is empty");
        }
        if (!fileName.contains(".")) {
            throw new MissingStatementException("File extension is empty");
        }
    }
}
