package com.marketplace.aggregatorservice.service;

import com.marketplace.aggregatorservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregatorService {
//
    private final RestTemplate restTemplate;

    private static final String URL_GET_PRODUCT = "http://51.250.102.12:9090/public/api/v1/products/{productId}";

    private static final String URL_GET_SELLER = "http://51.250.102.12:8085/public/api/v1/sellers/sellers/{externalId}";
    private static final String URL_CREATE_PRODUCT = "http://localhost:9090/public/api/v1/products/cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04/products";

    private static final String URL_CREATE_ORDER = "http://localhost:8081/public/api/v1/users/{userId}/orders";
    private static final String URL_GET_ORDER = "http://localhost:8081/public/api/v1/orders/{externalId}";
    private static final String URL_ENHANCED = "http://51.250.102.12:9090/public/api/v1/products/{externalId}";

    public String getProductDetails(String externalId) {

        String forObject = restTemplate.getForObject(URL_GET_PRODUCT, String.class);

        return forObject;
    }

    //получаем объект
    public ProductResponseDto getProductDtoWithYourId(String externalId) {
        return restTemplate.getForObject(URL_ENHANCED, ProductResponseDto.class, externalId);
    }

    //получаем
    public HttpHeaders getHttpHeaders() {
        return restTemplate.headForHeaders(URL_GET_PRODUCT);
    }

    //post запрос на создание
    public UUID postProduct() {
        ProductRequestDto productRequestDto =
                new ProductRequestDto("asd", "asdaaaaaaaaaaaaaa", "asd", 11L, 1L,
                        new TypeDto("asd", Set.of(new TypeCharacteristicDto("asd", ValueType.BOOLEAN)),
                                Set.of(new RoomDto("asd"))), Set.of(new ProductCharacteristicDto("asd", "asd", ValueType.BOOLEAN)),
                        "asd", TaxType.ABSENT, Set.of(new ProductImageDto("asd")),
                        true, false, Set.of(new ProductSpecialPriceDto(LocalDateTime.parse("2023-06-13T10:14:33"),
                        LocalDateTime.parse("2023-06-13T10:14:33"), 11L)));


        UUID uuid = restTemplate.postForObject(URL_CREATE_PRODUCT, productRequestDto, UUID.class);
        return uuid;
    }

    public URI postProductLocatin() {
        ProductRequestDto productRequestDto =
                new ProductRequestDto("asd", "asdaaaaaaaaaaaaa", "asd", 11L, 1L,
                        new TypeDto("asd", Set.of(new TypeCharacteristicDto("asd", ValueType.BOOLEAN)),
                                Set.of(new RoomDto("asd"))), Set.of(new ProductCharacteristicDto("asd", "asd", ValueType.BOOLEAN)),
                        "asd", TaxType.ABSENT, Set.of(new ProductImageDto("asd")),
                        true, false, Set.of(new ProductSpecialPriceDto(LocalDateTime.parse("2023-06-13T10:14:33"),
                        LocalDateTime.parse("2023-06-13T10:14:33"), 11L)));

        URI uri = restTemplate.postForLocation(URL_CREATE_PRODUCT, productRequestDto);
        return uri;
    }

//    public ProductResponseDto postProductexchange() {
//        HttpEntity<ProductRequestDto productRequestDto =
//                new ProductRequestDto("asd", "asdaaaaaaaaaaaaa", "asd", 11L, 1L,
//                        new TypeDto("asd", Set.of(new TypeCharacteristicDto("asd", ValueType.BOOLEAN)),
//                                Set.of(new RoomDto("asd"))), Set.of(new ProductCharacteristicDto("asd", "asd", ValueType.BOOLEAN)),
//                        "asd", TaxType.ABSENT, Set.of(new ProductImageDto("asd")),
//                        true, false, Set.of(new ProductSpecialPriceDto(LocalDateTime.parse("2023-06-13T10:14:33"),
//                        LocalDateTime.parse("2023-06-13T10:14:33"), 11L)));
//
//        ProductResponseDto productResponseDto = restTemplate.exchange(URL_CREATE, HttpMethod.POST, productRequestDto, ProductResponseDto.class);
//        return productResponseDto;
//    }

    public OrderResponseDto getOrder(String externalId) {

        ResponseEntity<OrderResponseDto> response = restTemplate.getForEntity(URL_GET_ORDER, OrderResponseDto.class, externalId);
        if (response.getStatusCode().is2xxSuccessful()) {
            if (response.hasBody()) {
                return response.getBody();
            }
            log.info("нету объекта");
        }
        log.error("получен статус {} при запросе получить заказ по externalId {}", response.getStatusCode(), externalId);
        throw new RuntimeException("заказ не найден по externalId = %s".formatted(externalId));
    }

    public SellerResponseDto getSeller(String externalId) {

        OrderResponseDto orderResponseDto = restTemplate.getForObject(URL_GET_ORDER, OrderResponseDto.class, externalId);
        UUID userId = orderResponseDto.userId();
        String urlGetSeller = "http://localhost:8085/public/api/v1/sellers/{userId}";
        SellerResponseDto sellerResponseDto = restTemplate.getForObject(urlGetSeller, SellerResponseDto.class, userId);
        return sellerResponseDto;
    }

    public SellerResponseDto getProduct(String externalId) {

        OrderResponseDto orderResponseDto = restTemplate.getForObject(URL_GET_ORDER, OrderResponseDto.class, externalId);
        UUID userId = orderResponseDto.userId();
        String urlGetSeller = "http://localhost:8085/public/api/v1/sellers/{userId}";
        SellerResponseDto sellerResponseDto = restTemplate.getForObject(urlGetSeller, SellerResponseDto.class, userId);

        return sellerResponseDto;
    }

    public UUID getProductExchange(String externalId) {

        ProductRequestDto productRequestDto = getProductRequestDto();

        ResponseEntity<UUID> responseEntity = restTemplate.exchange(URL_CREATE_PRODUCT, HttpMethod.POST, createHttpEntity(productRequestDto), UUID.class, externalId);
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }
        throw new RuntimeException("ошибка");
    }

    public ProductRequestDto getProductRequestDto() {
        return new ProductRequestDto("asd", "asdaaaaaaaaaaaaa", "asd", 11L, 1L,
                new TypeDto("asd", Set.of(new TypeCharacteristicDto("asd", ValueType.BOOLEAN)),
                        Set.of(new RoomDto("asd"))), Set.of(new ProductCharacteristicDto("asd", "asd",
                ValueType.BOOLEAN)),
                "asd", TaxType.ABSENT, Set.of(new ProductImageDto("asd")),
                true, false,
                Set.of(new ProductSpecialPriceDto(LocalDateTime.parse("2023-06-13T10:14:33"),
                        LocalDateTime.parse("2023-06-13T10:14:33"), 11L)));
    }

    public HttpEntity<ProductRequestDto> createHttpEntity(ProductRequestDto productRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(productRequestDto, headers);
    }

    public void getProductAndSellerDetails(String productId){
        ProductResponseDto productResponseDto = restTemplate.getForObject(URL_GET_PRODUCT, ProductResponseDto.class, productId);
//        UUID sellerId = productResponseDto.restTemplate.getForObject();

    }
}