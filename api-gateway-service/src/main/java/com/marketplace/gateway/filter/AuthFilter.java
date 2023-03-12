package com.marketplace.gateway.filter;

import com.marketplace.gateway.config.SecurityProperties;
import com.marketplace.gateway.service.OAuthAuthenticationService;
import com.marketplace.gateway.util.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final SecurityProperties securityProperties;
    private final OAuthAuthenticationService authenticationService;

    public AuthFilter(SecurityProperties securityProperties, OAuthAuthenticationService authenticationService) {
        super(Config.class);
        this.authenticationService = authenticationService;
        this.securityProperties = securityProperties;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info(exchange.getRequest().getPath().value());

            if (!securityProperties.getWhiteList().contains(exchange.getRequest().getPath().value())) {
                String userId = exchange.getRequest().getHeaders().getFirst(AuthConstants.X_USER_ID_HEADER);
                String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (StringUtils.hasText(bearerToken) && StringUtils.hasText(userId)) {
                    String token = bearerToken.replaceFirst(BEARER.getValue(), "").trim();

                    if (!authenticationService.isAuthenticated(userId, token)) {
                        throw new AuthorizationServiceException("User is not authenticated");
                    }

                } else {
                    throw new AuthorizationServiceException("X-USER-ID Header or Authorization headers are not provided");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {}
}