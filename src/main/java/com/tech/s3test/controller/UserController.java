package com.tech.s3test.controller;

import com.tech.s3test.dto.req.UpdateUserReqDto;
import com.tech.s3test.dto.req.UserReqDto;
import com.tech.s3test.dto.res.JwtResDto;
import com.tech.s3test.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserReqDto user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResDto> login(@Valid @RequestBody UserReqDto user) {
        return ResponseEntity.ok().body(userService.login(user));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserReqDto user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }
}
