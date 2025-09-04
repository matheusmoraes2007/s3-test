package com.tech.s3test.service;

import com.tech.s3test.dto.res.SaveFileResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Service s3Service;

    public SaveFileResDto saveFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        this.validateFileName(originalFileName);
        String fileExtension = originalFileName.substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        s3Service.save(file, fileName);
        return new SaveFileResDto(fileName);
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
