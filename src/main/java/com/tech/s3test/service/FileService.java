package com.tech.s3test.service;

import com.tech.s3test.adapter.storage.StoragePort;
import com.tech.s3test.configuration.log.Log;
import com.tech.s3test.dto.res.FileResDto;
import com.tech.s3test.dto.res.SaveFileResDto;
import com.tech.s3test.exception.custom.MissingStatementException;
import com.tech.s3test.model.FileModel;
import com.tech.s3test.model.UserModel;
import com.tech.s3test.repository.FileRepository;
import com.tech.s3test.util.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final StoragePort storagePort;
    private final UserUtils userUtils;
    private final FileRepository fileRepository;

    @Log
    @Transactional
    public SaveFileResDto save(MultipartFile file) {
        UserModel user = userUtils.findAuthenticatedUser();
        String key = this.getKey(file);
        FileModel fileModel = new FileModel(user, key);
        storagePort.save(key, file);
        try {
            fileRepository.save(fileModel);
        } catch (Exception e) {
            storagePort.delete(key);
            throw e;
        }
        return new SaveFileResDto(key);
    }

    @Log
    public FileResDto getFile(String key) {
        this.assertFileOwnership(key);
        return storagePort.get(key);
    }

    @Log
    public void putUpdateByKey(String key, MultipartFile file) {
        this.assertFileOwnership(key);
        storagePort.update(key, file);
    }

    @Log
    @Transactional
    public void deleteByKey(String key) {
        this.assertFileOwnership(key);
        storagePort.delete(key);
        fileRepository.deleteByKey(key);
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

    private void assertFileOwnership(String key) {
        Long userId = userUtils.findAuthenticatedUser().getId();
        if (!fileRepository.existsByKeyAndUserId(key, userId)) {
            throw new RuntimeException("File not found");
        }
    }
}
