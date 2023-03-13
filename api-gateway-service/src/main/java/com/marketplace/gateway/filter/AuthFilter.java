package com.marketplace.gateway.filter;

import com.marketplace.gateway.exception.AuthenticationException;
import com.marketplace.gateway.exception.MissingAuthHeaderException;
import com.marketplace.gateway.service.OAuthAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;

import static com.marketplace.gateway.util.AuthConstants.X_USER_ID_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final List<PathPattern> whiteList;
    private final OAuthAuthenticationService authenticationService;

    public AuthFilter(List<PathPattern> whiteList, OAuthAuthenticationService authenticationService) {
        super(Config.class);
        this.authenticationService = authenticationService;
        this.whiteList = whiteList;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            PathContainer requestedPath = exchange.getRequest().getPath().pathWithinApplication();
            log.info(requestedPath.value());

            if (whiteList.stream().noneMatch(pattern -> pattern.matches(requestedPath))) {
                String userId = exchange.getRequest().getHeaders().getFirst(X_USER_ID_HEADER);
                String bearerToken = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);

                if (StringUtils.hasText(bearerToken) && StringUtils.hasText(userId)) {
                    String token = bearerToken.replaceFirst(BEARER.getValue(), "").trim();

                    if (!authenticationService.isAuthenticated(userId, token)) {
                        throw new AuthenticationException("User is not authenticated");
                    }

                } else {
                    throw new MissingAuthHeaderException("X-User-Id and Authorization headers must be provided");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {}
}