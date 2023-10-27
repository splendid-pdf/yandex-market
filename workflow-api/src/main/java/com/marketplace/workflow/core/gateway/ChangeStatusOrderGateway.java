package com.marketplace.workflow.core.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ChangeStatusOrderGateway {

    private final RestTemplate restTemplate;

    public HttpStatusCode changeStatusOrderOperation(String orderId) {
        return restTemplate.exchange(
                "http://localhost:8081/public/api/v1/sellers/orders/" + orderId + "/send",
                HttpMethod.PUT,
                null,
                HttpStatusCode.class
        ).getStatusCode();
    }
}
