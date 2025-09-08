package com.tech.s3test.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserReqDto(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email invalid format")
        @Size(min = 5, max = 50, message = "email must be between 5 and 100 characters")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 20, message = "password must be between 5 and 100 characters")
        String password
) {
}
