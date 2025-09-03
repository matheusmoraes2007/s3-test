package com.tech.s3test.dto.req;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record fileReqDto(
        @NotNull(message = "File cannot be null")
        MultipartFile file
) {
}
