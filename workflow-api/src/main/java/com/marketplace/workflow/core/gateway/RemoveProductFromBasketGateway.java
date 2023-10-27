package com.marketplace.workflow.core.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveProductFromBasketGateway {
    private final RestTemplate restTemplate;

    public HttpStatusCode removeProductFromBasket(String userId, List<String> productIds) {
        HttpEntity<List<String>> entity = new HttpEntity<>(productIds);

        return restTemplate.exchange(
                "http://localhost:9091/public/api/v1/users/" + userId + "/basket/products",
                HttpMethod.DELETE,
                entity,
                HttpStatusCode.class
        ).getStatusCode();
    }
}
