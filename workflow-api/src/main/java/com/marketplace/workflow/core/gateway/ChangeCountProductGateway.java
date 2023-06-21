package com.marketplace.workflow.core.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ChangeCountProductGateway {

    private final RestTemplate restTemplate;

    public HttpStatusCode changeCountProduct(String sellerId, String productId, Long count) {
//        return restTemplate.patchForObject(
//                "http://localhost:9090/public/api/v1/sellers/{}/products/{}/count?count={}",
//                String.class,
//                sellerId, productId, count
//        ).getStatusCode();

//        restTemplate.put(
//                "http://localhost:9090/public/api/v1/sellers/" + sellerId +
//                        "/products/" + productId +
//                        "/count?count=" + count,
//                String.class,
//                String.class,
//                sellerId, productId, count
//        );

//        return HttpStatusCode.valueOf(204);

        return restTemplate.exchange(
                "http://localhost:9090/public/api/v1/sellers/" + sellerId +
                        "/products/" + productId +
                        "/count?count=" + count,
                HttpMethod.PUT,
                null,
                HttpStatusCode.class
        ).getStatusCode();
    }
}