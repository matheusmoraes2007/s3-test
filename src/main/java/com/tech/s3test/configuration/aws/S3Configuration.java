package com.tech.s3test.configuration.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Configuration {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .build();
    }
}
