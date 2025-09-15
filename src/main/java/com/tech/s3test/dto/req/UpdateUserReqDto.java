package com.tech.s3test.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserReqDto(
        @Email(message = "Email invalid format")
        @Size(min = 5, max = 50, message = "email must be between 5 and 100 characters")
        String email,

        @Size(min = 8, max = 20, message = "password must be between 5 and 100 characters")
        String password
) {
}
