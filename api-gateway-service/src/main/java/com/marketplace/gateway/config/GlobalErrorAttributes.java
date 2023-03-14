package com.marketplace.gateway.config;

import com.marketplace.gateway.exception.AuthenticationException;
import com.marketplace.gateway.exception.MissingAuthHeaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Slf4j
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Throwable getError(ServerRequest request) {
        return super.getError(request);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.MESSAGE));
        int statusCode = getStatusCode(getError(request));
        map.put("status", statusCode != -1 ? statusCode : map.get("status"));
        map.remove("error");

        log.error("Error with requestId '%s' with message: '%s' - ".formatted(map.get("requestId"), map.get("message")));

        return map;
    }

    private int getStatusCode(Throwable throwable) {
        if (throwable instanceof MissingAuthHeaderException) {
            return MissingAuthHeaderException.STATUS_CODE;
        } else if (throwable instanceof AuthenticationException) {
            return AuthenticationException.STATUS_CODE;
        }

        return -1;
    }
}