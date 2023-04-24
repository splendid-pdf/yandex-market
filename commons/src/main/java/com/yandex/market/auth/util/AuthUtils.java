package com.yandex.market.auth.util;

import org.springframework.security.oauth2.jwt.JoseHeaderNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.yandex.market.auth.util.TestClientAttributes.USER;

public class AuthUtils {

    public static JwtAuthenticationToken token(String id, String role) {
        Jwt jwt = Jwt.withTokenValue(USER.accessToken())
                .header(JoseHeaderNames.KID, UUID.randomUUID().toString())
                .claim(ClientAttributes.USER_ID, id)
                .claim("authorities", List.of(role))
                .build();
        return new JwtAuthenticationToken(jwt, Collections.emptyList());
    }
}
