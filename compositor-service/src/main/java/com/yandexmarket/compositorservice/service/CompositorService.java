package com.yandexmarket.compositorservice.service;

import com.yandex.market.util.RestPageImpl;
import com.yandexmarket.compositorservice.dto.ProductPreview;
import com.yandexmarket.compositorservice.dto.ProductPreviewMainPage;
import com.yandexmarket.compositorservice.dto.SellerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompositorService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final static String PRODUCT_PREVIEWS_URL = "http://localhost:9090/public/api/v1/product-previews?page=0,size=3,sort=DESC";
    private final static String SELLER_URL = "http://localhost:8085/public/api/v1/sellers/{sellerId}";
    private final static String TOKEN = "Bearer eyJraWQiOiI0OTczZGI0Ny1lZmZmLTQ5OTItOTlkOC0xOTczOWM4MDBjNjUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhc2QxMUBtYWlsLnJ1IiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjgwMTcyOTI4LCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzUxLjI1MC4xMDIuMTI6OTAwMCIsImV4cCI6MTY4MDM4ODkyOCwiaWF0IjoxNjgwMTcyOTI4LCJzZWxsZXItaWQiOiJhMTFlY2Y2OS1iZjFiLTRiOTctYmFjNy04NGFmOWU0ODlkZmQiLCJhdXRob3JpdGllcyI6WyJST0xFX1NFTExFUiJdfQ.Hk3Oog3QZI16BzkSsmM2V9vRIpbJLhzLPKVqn4WslBsAiom2ap-aC0ZDD1AoN69YpZ4k3vbjb0HKkMOSgukHTPl6thFTqhNwH547PpvLeEpH0m7z5yexStpfWcxBXfGU-1q9zXfG7HJzVub97kO1h7mKZ0nArY4xycmi7c40bFk4Fu5NT_TLYjCk4xnzUzPiuMETt2hZHzt9wswTHdQM5DrJivIWXjG1KhQuteMLlchU4Y2qCGepSzTjaYa6_oDFa9Ni9Rw374qdDN1cigJ8zGsdrNk99_oh2paHN7XIzvE9zT9SL6W4ZoG6FCAf62cKJEKWJQ7Zneh6ju3Sy0igEA";

    public List<ProductPreviewMainPage> getNewestProducts() {

        RestPageImpl<ProductPreview> restResponsePage = webClient.get()
                .uri(PRODUCT_PREVIEWS_URL)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestPageImpl<ProductPreview>>() {
                })
                .block();
        log.info(restResponsePage.getContent().toString());

        return restResponsePage.getContent().stream()
                .map(this::mapping)
                .toList();
    }

    private String getCompanyName(String sellerId) {

        SellerResponseDto sellerResponseDto = webClient.get()
                .uri(SELLER_URL, sellerId)
                .header(AUTHORIZATION, TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SellerResponseDto>() {
                })
                .block();

        return sellerResponseDto.companyName();
    }

    private ProductPreviewMainPage mapping(ProductPreview productPreview) {

        String companyName = getCompanyName(productPreview.sellerId());

        return new ProductPreviewMainPage(
                productPreview.id(),
                productPreview.sellerId(),
                productPreview.name(),
                productPreview.price(),
                productPreview.imageUrl(),
                companyName
        );
    }
}