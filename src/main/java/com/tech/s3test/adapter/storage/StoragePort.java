package com.tech.s3test.adapter.storage;

import com.tech.s3test.configuration.aws.BucketProperties;
import com.tech.s3test.dto.res.FileResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StoragePort implements StorageAdapter {
    private final S3Client s3Client;
    private final BucketProperties bucketProperties;

    @Override
    public void save(String key, MultipartFile file) {
        if (this.verifyIfExists(key))
            throw new RuntimeException("File already exists");
        this.putObject(key, file);
    }

    @Override
    public FileResDto get(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketProperties.getBucketName())
                    .key(key)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return new FileResDto(
                    s3Object.readAllBytes(),
                    s3Object.response().contentType()
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean verifyIfExists(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketProperties.getBucketName())
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException ex) {
            return false;
        }
    }

    private void putObject(String key, MultipartFile file) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketProperties.getBucketName())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
