package com.yandex.market.basketservice.models;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

public class Environment {

    public static final String
            BASKET_PATH = "/" + PUBLIC_API_V1 + "/users/{userId}/basket/products";

    public static final String REAL_USER_ID = "6a2e63a7-a8b7-4a5e-9422-6a16ee963e8d";

    public static final String AUTH_TOKEN = "eyJraWQiOiI1OGY1NmJlNS01NTcxLTQzYzYtOGViMS0zMmFjNzJkMjMzY2UiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb2NrQHJvY2sucnUiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2ODI2ODE3NzYsInNjb3BlIjpbIm9wZW5pZCJdLCJ1c2VyLWlkIjoiNmEyZTYzYTctYThiNy00YTVlLTk0MjItNmExNmVlOTYzZThkIiwiaXNzIjoiaHR0cDovLzUxLjI1MC4xMDIuMTI6OTAwMCIsImV4cCI6MTY4MzI4NjU3NiwiaWF0IjoxNjgyNjgxNzc2LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXX0.q6nZS3ZBbtj2wIMEu7BU-w0EWkqBbF2UIlheqNPYuTOYDGE4igsgS4xFUESmDbK0YWv7ykbcaWBav6e61Y8KOmGZ7DBrIqiEnhW_wozX64tUBe5ZHonkTcnNwuetv4I5GCptIfK_SOy2PxeZDnEMNZTobtPRfRHNBAj4BwtbWudKRy2ieBH_4jHjsInhxMTWbQ1QY-O4VyY7k9uWb0oAMLRYsPaIhQuAKucYERgnU-HnPgEd1l61EG9yFxscrZq819hxuyjhx4FOyVtrbkRbwhBb93Dp4jA1jLJq32FEFfZVyeEpREnGGziVzn6_DP63_p2izfNjt8Gx9ED4QP5_aQ";
}
