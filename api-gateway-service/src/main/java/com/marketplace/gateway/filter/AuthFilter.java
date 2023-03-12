package com.marketplace.gateway.filter;

import com.marketplace.gateway.service.OAuthAuthenticationService;
import com.marketplace.gateway.util.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final OAuthAuthenticationService authenticationService;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String userId = exchange.getRequest().getHeaders().getFirst(AuthConstants.X_USER_ID_HEADER);

            if (!authenticationService.isAuthenticated(userId, token)) {
                throw new AuthorizationServiceException("User is not authenticated");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}