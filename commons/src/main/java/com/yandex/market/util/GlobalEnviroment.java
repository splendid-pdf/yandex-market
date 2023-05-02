package com.yandex.market.util;

public final class GlobalEnviroment {
    public static final String SECURITY_FORBIDDEN_MESSAGE = """
            {
                "code": 403,
                "error": "Access is denied",
                "message": "Sorry, you do not have permission to access this resource. Please login with your account profile to gain access."
            }
            """;
}
