package com.yandex.market.userinfoservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app.validation.error-info")
@Setter
public class ErrorInfoProperties {
    @NotNull
    private Map<String, String> errorInfos;

    public String getMessageByErrorCode(String errorCode){
        return errorInfos.getOrDefault(errorCode, StringUtils.EMPTY);
    }

}
