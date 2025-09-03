package com.tech.s3test.controller;

import com.tech.s3test.dto.req.fileReqDto;
import com.tech.s3test.service.FileService;
import com.tech.s3test.utils.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final ControllerUtils controllerUtils;
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@Valid @RequestParam("file") fileReqDto file) {
        String requestId = controllerUtils.generateUUID();
        log.info("ID[{}] Request received", requestId);
        fileService.saveFile(file, requestId);
    }
}
