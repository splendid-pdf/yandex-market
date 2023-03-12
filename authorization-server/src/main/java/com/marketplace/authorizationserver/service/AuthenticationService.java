package com.marketplace.authorizationserver.service;

import com.marketplace.authorizationserver.config.properties.SecurityProperties;
import com.marketplace.authorizationserver.dto.OAuthUserData;
import com.marketplace.authorizationserver.dto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokenService tokenService;
    private final UserAuthenticationProvider provider;
    private final SecurityProperties securityProperties;
    private final OAuth2AuthorizationService authorizationService;

    @SuppressWarnings("unchecked")
    public OAuthUserData authenticate(UserLoginRequest request) {
        Authentication authentication = provider.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String email = authentication.getPrincipal().toString();
        String userId = ((Map<String, String>) authentication.getDetails()).get("user-id");

        if (authentication.isAuthenticated()) {
            OAuth2AccessToken accessToken =
                    tokenService.createAccessToken(authentication, securityProperties.getAccessTokenLifetimeInMinutes());
            OAuth2RefreshToken refreshToken =
                    tokenService.createRefreshToken(authentication, securityProperties.getRefreshTokenLifetimeInMinutes());

            OAuth2Authorization authorizationObject = getoAuth2Authorization(email, userId, accessToken, refreshToken);

            authorizationService.save(authorizationObject);

            return new OAuthUserData(
                    userId,
                    email,
                    accessToken.getTokenValue(),
                    refreshToken.getTokenValue(),
                    accessToken.getExpiresAt()
            );
        }

        throw new AuthorizationServiceException("User with id '%s' wasn't authenticated".formatted(userId));
    }

    public void revokeAuthentication(String token, OAuth2TokenType tokenType) {
        OAuth2Authorization authorization = null;
        if (StringUtils.hasText(token)) {
            token = token.contains(BEARER.getValue())
                    ? token.replaceFirst(BEARER.getValue(), "").trim()
                    : token;
            authorization = authorizationService.findByToken(token, tokenType);

            if (authorization != null) {
                authorizationService.remove(authorization);
            } else {
                throw new AuthorizationServiceException("You were not authenticated");
            }
        } else {
            throw new AuthorizationServiceException("Token is not provided");
        }
    }

    @SneakyThrows
    private OAuth2Authorization getoAuth2Authorization(
            String email,
            String userId,
            OAuth2AccessToken accessToken,
            OAuth2RefreshToken refreshToken
    ) {
        Class<? extends OAuth2Authorization> authorizationClass = OAuth2Authorization.class;
        Constructor<? extends OAuth2Authorization> defaultConstructor = authorizationClass.getDeclaredConstructor();
        defaultConstructor.setAccessible(true);
        OAuth2Authorization authorizationObject = defaultConstructor.newInstance();

        Field fieldId = authorizationClass.getDeclaredField("id");
        Field fieldClientId = authorizationClass.getDeclaredField("registeredClientId");
        Field fieldPrincipalName = authorizationClass.getDeclaredField("principalName");
        Field fieldGrantType = authorizationClass.getDeclaredField("authorizationGrantType");
        Field fieldScopes = authorizationClass.getDeclaredField("authorizedScopes");
        Field fieldTokens = authorizationClass.getDeclaredField("tokens");
        Field fieldAttributes = authorizationClass.getDeclaredField("attributes");

        Arrays.stream(authorizationClass.getDeclaredFields()).forEach(field -> field.setAccessible(true));

        fieldId.set(authorizationObject, userId);
        fieldClientId.set(authorizationObject, securityProperties.getClientId());
        fieldPrincipalName.set(authorizationObject, email);
        fieldGrantType.set(authorizationObject, AuthorizationGrantType.PASSWORD);
        fieldScopes.set(authorizationObject, Set.of("openid"));

        Class<?> tokenClass = OAuth2Authorization.class.getDeclaredClasses()[0];
        Constructor<?> tokenConstructor = tokenClass.getDeclaredConstructors()[0];
        tokenConstructor.setAccessible(true);
        Object accessTokenRepresentation = tokenConstructor.newInstance(accessToken);
        Object refreshTokenRepresentation = tokenConstructor.newInstance(refreshToken);

        fieldTokens.set(authorizationObject, Map.of(
                OAuth2AccessToken.class, accessTokenRepresentation,
                OAuth2RefreshToken.class, refreshTokenRepresentation
        ));
        fieldAttributes.set(authorizationObject, null);
        return authorizationObject;
    }
}