package com.marketplace.authorizationserver.config.properties;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private ClientSettings clientSettings;
    private TokenSettings tokenSettings;

    @Setter
    static class ClientSettings {
        private String clientId;
        private String clientSecret;
    }

    static class TokenSettings {
        private Integer expirationTimeInMinutes;
    }

    public String getClientId() {
        return clientSettings.clientId;
    }

    public String getClientSecret() {
        return clientSettings.clientSecret;
    }

    public Integer getExpirationTimeInMinutes() {
        return tokenSettings.expirationTimeInMinutes;
    }
}
