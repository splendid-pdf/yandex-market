package com.yandex.market.auth.service;

import com.yandex.market.auth.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

import static com.yandex.market.auth.util.ClientAttributes.SELLER_ID;
import static com.yandex.market.auth.util.ClientAttributes.USER_ID;

@Slf4j
public class PermissionService {

    public boolean hasPermission(UUID id, Role role, String idAttribute) {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        return !tokenAttributes.isEmpty() && checkPermission(id, role, idAttribute, tokenAttributes);
    }

    @SuppressWarnings("unchecked")
    public boolean hasPermission() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        List<String> authorities = (List<String>) tokenAttributes.getOrDefault("authorities", Collections.emptyList());
        return Arrays.stream(Role.values())
                .map(Role::getKey)
                .anyMatch(authorities::contains);
    }

    private Map<String, Object> getTokenAttributes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            return authenticationToken.getTokenAttributes();
        }

        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    private boolean checkPermission(
            UUID id,
            Role role,
            String idAttribute,
            Map<String, Object> tokenAttributes
    ) {
        String clientId = (String) tokenAttributes.get(idAttribute);
        if (StringUtils.isNotBlank(clientId)) {
            List<String> authorities = (List<String>) tokenAttributes.get("authorities");
            if (authorities.contains(Role.ADMIN.getKey())) {
                return true;
            } else if (clientId.equals(id.toString()) && authorities.contains(role.getKey())) {
                return true;
            }
        }

        log.debug("Client with id {} has no permission for resource", id);
        return false;
    }
}