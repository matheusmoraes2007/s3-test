package com.tech.s3test.adapter.storage;

import com.tech.s3test.configuration.aws.BucketProperties;
import com.tech.s3test.dto.res.FileResDto;
import com.tech.s3test.exception.custom.InternalServerErrorException;
import com.tech.s3test.exception.custom.ResourceAlreadyExistsException;
import com.tech.s3test.exception.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class StoragePort implements StorageAdapter {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final BucketProperties bucketProperties;

    @Override
    public void save(String key, MultipartFile file) {
        if (this.verifyIfExists(key))
            throw new ResourceAlreadyExistsException("File already exists");
        this.putObject(key, file);
    }

    @Override
    public FileResDto get(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketProperties.getBucketName())
                    .key(key)
                    .build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .getObjectRequest(getObjectRequest)
                    .build();
            return new FileResDto(
                    s3Presigner.presignGetObject(getObjectPresignRequest)
                            .url()
                            .toString()
            );
        } catch (NoSuchKeyException e) {
            throw new ResourceNotFoundException("File not found");
        }
    }

    @Override
    public void update(String key, MultipartFile file) {
        if (!this.verifyIfExists(key))
            throw new ResourceNotFoundException("File not found");
        this.putObject(key, file);
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketProperties.getBucketName())
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
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
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
