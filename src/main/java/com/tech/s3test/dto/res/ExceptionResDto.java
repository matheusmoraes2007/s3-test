package com.tech.s3test.dto.res;

public record ExceptionResDto(
        int status,
        String error
) {
}
