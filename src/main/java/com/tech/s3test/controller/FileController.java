package com.tech.s3test.controller;

import com.tech.s3test.dto.res.FileResDto;
import com.tech.s3test.dto.res.SaveFileResDto;
import com.tech.s3test.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SaveFileResDto> save(@RequestPart("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.save(file));
    }

    @GetMapping("/{key}")
    public ResponseEntity<byte[]> downloadByKey(@PathVariable("key") String key) {
        FileResDto file = fileService.downloadByKey(key);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .body(file.file());
    }

    @PutMapping(value = "/{key}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> putUpdateByKey(
            @PathVariable("key") String key,
            @RequestPart("file") MultipartFile file
    ) {
        fileService.putUpdateByKey(key, file);
        return ResponseEntity
                .ok()
                .build();
    }
}
