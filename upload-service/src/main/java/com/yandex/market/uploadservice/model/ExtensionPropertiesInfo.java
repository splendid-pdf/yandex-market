package com.yandex.market.uploadservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ExtensionPropertiesInfo {
    private Map<String, ExtensionMaxSizeInfo> extension;
}