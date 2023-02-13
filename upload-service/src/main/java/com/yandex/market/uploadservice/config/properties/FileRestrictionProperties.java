package com.yandex.market.uploadservice.config.properties;

import com.yandex.market.uploadservice.model.FileSizeRestriction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class FileRestrictionProperties {
    private Map<String, FileSizeRestriction> extension;
}