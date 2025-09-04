package com.tech.s3test.controller;

import com.tech.s3test.dto.req.FileResDto;
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
    public ResponseEntity<SaveFileResDto> upload(@RequestPart("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.saveFile(file));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable("fileName") String fileName) {
        FileResDto file = fileService.downloadFile(fileName);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.file());
    }
}
