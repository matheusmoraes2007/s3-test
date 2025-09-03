package com.tech.s3test.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ControllerUtils {
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
