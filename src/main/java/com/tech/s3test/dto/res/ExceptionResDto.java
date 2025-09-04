package com.tech.s3test.dto.res;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ExceptionResDto(
        int status,
        String error,
        OffsetDateTime timestamp
) {
}
