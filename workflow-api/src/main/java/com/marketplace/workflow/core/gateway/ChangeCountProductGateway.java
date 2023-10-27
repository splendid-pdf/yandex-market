package com.marketplace.workflow.core.gateway;

import com.marketplace.workflow.dto.ProductCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeCountProductGateway {

    private final RestTemplate restTemplate;

    public ResponseEntity changeCountProduct(String sellerId, List<ProductCountDto> productCountList) {

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ProductCountDto>> entity = new HttpEntity<>(productCountList);

        return restTemplate.exchange(
                "http://localhost:9090/public/api/v1/sellers/" + sellerId + "/products/counts",
                HttpMethod.PUT,
                entity,
                List.class
        );
    }


}