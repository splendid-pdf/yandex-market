package com.yandex.market.uploadservice.config.properties;

import com.yandex.market.uploadservice.model.FileSizeRestriction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class FileRestrictionProperties {
    //todo: посмотреть можно ли значение сделать лонговым, чтобы лишний класс не создавать
    private Map<String, FileSizeRestriction> extension;
}