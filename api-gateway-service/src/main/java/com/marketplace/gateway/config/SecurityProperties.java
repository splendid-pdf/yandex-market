package com.marketplace.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {
    private List<OpenedRoute> whiteList;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class OpenedRoute {
        private String url;
        private List<String> methods;
    }
}