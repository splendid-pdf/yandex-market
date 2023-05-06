package com.yandex.market.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.yandex.market.util.GlobalEnviroment.SECURITY_FORBIDDEN_MESSAGE;

public class SecurityUtils {
    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static AccessDeniedHandler resourceAccessDenied() {
        return (request, response, exception) -> {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(SECURITY_FORBIDDEN_MESSAGE);
        };
    }
}
