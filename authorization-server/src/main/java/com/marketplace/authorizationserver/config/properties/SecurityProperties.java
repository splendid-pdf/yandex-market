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
    private String dataProviderUri;

    @Setter
    static class ClientSettings {
        private String clientId;
        private String clientSecret;
        private String redirectLoginUri;
        private String redirectSuccessLoginUri;
    }

    @Setter
    static class TokenSettings {
        private Integer accessTokenLifetimeInMinutes;
        private Integer refreshTokenLifetimeInMinutes;
    }

    public String getClientId() {
        return clientSettings.clientId;
    }

    public String getClientSecret() {
        return clientSettings.clientSecret;
    }

    public String getRedirectLoginUri() {
        return clientSettings.redirectLoginUri;
    }

    public String getDataProviderUri() {
        return dataProviderUri;
    }

    public String getRedirectSuccessLoginUri() {
        return clientSettings.redirectSuccessLoginUri;
    }

    public Integer getAccessTokenLifetimeInMinutes() {
        return tokenSettings.accessTokenLifetimeInMinutes;
    }

    public Integer getRefreshTokenLifetimeInMinutes() {
        return tokenSettings.refreshTokenLifetimeInMinutes;
    }

}