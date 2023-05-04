package com.yandex.market.uploadservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.validation")
public class ValidationProperties {
    private Map<String, FileRestriction> extensions;
    private int maxUploadedFilesCount;
    private int maxDownloadedFilesCount;
}