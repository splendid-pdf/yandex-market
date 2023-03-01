package com.yandex.market.userinfoservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PermissionService {

    public boolean hasPermission(UUID externalId) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        Map<String, Object> attributes = authenticationToken.getTokenAttributes();
        String authUserExternalId = (String) attributes.get("user-id");
        return authUserExternalId.equals(externalId.toString());
    }
}