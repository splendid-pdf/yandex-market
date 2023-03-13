package com.yandex.market.userservice.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Setter
@Validated
public class ErrorInfoProperties {
    @NotNull
    private Map<String, String> errorInfo;

    public String getMessageByErrorCode(String errorCode) {
        return errorInfo.getOrDefault(errorCode, StringUtils.EMPTY);
    }
}