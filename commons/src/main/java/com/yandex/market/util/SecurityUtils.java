package com.yandex.market.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.LocalDateTime;
import java.util.UUID;

public class SecurityUtils {
    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static AccessDeniedHandler resourceAccessDenied() {
        return (request, response, exception) -> {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            String responseBody = "{\n" +
                    "  \"timestamp\": \"" + LocalDateTime.now() + "\",\n" +
                    "  \"path\": \"" + request.getRequestURI() + "\",\n" +
                    "  \"status\": " + HttpStatus.FORBIDDEN.value() + ",\n" +
                    "  \"error\": \"Access is denied\",\n" +
                    "  \"message\": \"Sorry, you do not have permission to access this resource. Please login with your account profile to gain access.\",\n" +
                    "  \"requestId\": \"" + UUID.randomUUID() + "\",\n" +
                    "  \"exception\": \"" + exception.getClass().getName() + "\"\n" +
                    "}";
            response.getWriter().write(responseBody);
        };
    }
}
