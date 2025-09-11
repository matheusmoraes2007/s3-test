package com.tech.s3test.util;

import com.tech.s3test.exception.custom.ResourceNotFoundException;
import com.tech.s3test.model.UserModel;
import com.tech.s3test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUtils {
    private final UserRepository userRepository;

    public UserModel findAuthenticatedUser() {
        return this.findUserByEmail(AuthUtils.getEmail());
    }

    private UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
