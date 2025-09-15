package com.tech.s3test.dto.res;

public record JwtResDto(
        String prefix,
        String token,
        Long expiresAt
) {
}
