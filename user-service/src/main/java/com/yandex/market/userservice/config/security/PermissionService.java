package com.yandex.market.userservice.config.security;

import com.yandex.market.userservice.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionService {

    @SuppressWarnings("unchecked")
    public boolean hasPermission(UUID externalId) {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        String authUserExternalId = (String) tokenAttributes.get("user-id");
        List<String> authorities = (List<String>) tokenAttributes.get("authorities");
        if (authorities.contains(Role.ADMIN.getKey())){
            return true;
        } else if (authUserExternalId.equals(externalId.toString()) && authorities.contains(Role.USER.getKey())) {
            return true;
        }

        log.info("User with id {} has no permission for resource", externalId);
        return false;
    }

    private Map<String, Object> getTokenAttributes() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        log.info("Access token: " + authenticationToken.getToken().getTokenValue());
        return authenticationToken.getTokenAttributes();
    }
}