package com.tech.s3test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public void saveFile(MultipartFile file, String requestId) {
        String originalFileName = file.getOriginalFilename();
        this.validateFileName(originalFileName);
        String fileExtension = originalFileName.substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        this.s3Save(file, fileName);
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("File name is empty");
        }
        if (fileName.contains(".")) {
            throw new RuntimeException("File extension is empty");
        }
    }

    private void s3Save(MultipartFile file, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
