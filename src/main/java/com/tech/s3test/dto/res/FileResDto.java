package com.tech.s3test.dto.res;

public record FileResDto(
        byte[] file,
        String contentType
) {
}
