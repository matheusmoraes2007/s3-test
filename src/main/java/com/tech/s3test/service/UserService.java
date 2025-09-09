package com.tech.s3test.service;

import com.tech.s3test.configuration.security.AuthUtils;
import com.tech.s3test.dto.req.UpdateUserReqDto;
import com.tech.s3test.dto.req.UserReqDto;
import com.tech.s3test.dto.res.JwtResDto;
import com.tech.s3test.exception.custom.ResourceAlreadyExistsException;
import com.tech.s3test.exception.custom.ResourceNotFoundException;
import com.tech.s3test.model.UserModel;
import com.tech.s3test.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void createUser(UserReqDto reqDto) {
        this.verifyIfExistsByEmail(reqDto.email());
        UserModel newUser = new UserModel(
                reqDto.email(),
                passwordEncoder.encode(reqDto.password()),
                UUID.randomUUID().toString()
        );
        userRepository.save(newUser);
    }

    public JwtResDto login(UserReqDto reqDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(reqDto.email(), reqDto.password());
        authenticationManager.authenticate(token);
        return jwtService.generateToken(reqDto.email());
    }

    @Transactional
    public void updateUser(UpdateUserReqDto reqDto) {
        UserModel User = this.getUserByEmail(AuthUtils.getEmail());
        if (reqDto.email() != null && !reqDto.email().isBlank() && !reqDto.email().equals(AuthUtils.getEmail())) {
            this.verifyIfExistsByEmail(reqDto.email());
            User.setEmail(reqDto.email());
        }
        if (reqDto.password() != null && !reqDto.password().isBlank()) {
            User.setPassword(passwordEncoder.encode(reqDto.password()));
        }
    }

    private UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void verifyIfExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
    }
}
