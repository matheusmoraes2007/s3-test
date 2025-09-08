package com.tech.s3test.configuration.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class BucketProperties {
    private String bucketName;
}
