package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.minio")
@Data
public class MinioProperties {

    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String bucketName;
}
