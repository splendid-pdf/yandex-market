package com.yandex.market.uploadservice.config.properties;

import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.object-storage")
public class ObjectStorageProperties {

    private String host;
    private String region;
    private String bucketName;
    @Value("${application.object-storage.credentials.secret-key}")
    String secretKey;
    @Value("${application.object-storage.credentials.access-key}")
    String accessKey;
//    private BasicAWSCredentials credentials;
    private Integer urlExpirationTimeInYears;
    private Integer maximumFilesCount;
}