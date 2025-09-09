package com.tech.s3test.service;

import com.tech.s3test.dto.req.UserReqDto;
import com.tech.s3test.exception.custom.ResourceAlreadyExistsException;
import com.tech.s3test.model.UserModel;
import com.tech.s3test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserReqDto reqDto) {
        if (userRepository.existsByEmail(reqDto.email())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        UserModel newUser = new UserModel(
                reqDto.email(),
                passwordEncoder.encode(reqDto.password()),
                UUID.randomUUID().toString()
        );
        userRepository.save(newUser);
    }
}
