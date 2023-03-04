package com.yandex.market.userservice.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Component
@Validated
@ConfigurationProperties(prefix = "app.api.rest")
public class RestProperties {
    @NotNull
    private Geocoder geocoder;
    private Integer connectionTimeoutInMs;
    private Integer readConnectionTimeoutInMs;

    public record Geocoder(String url, String apiKey) {}

    public String geocoderApiKey() {
        return geocoder.apiKey;
    }

    public String geocoderUrl() {
        return geocoder.url;
    }
}

