package com.tech.s3test.dto.req;

public record FileResDto(
        byte[] file,
        String contentType
) {
}
