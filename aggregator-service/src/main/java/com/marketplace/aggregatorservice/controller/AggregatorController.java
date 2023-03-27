package com.marketplace.aggregatorservice.controller;

import com.marketplace.aggregatorservice.dto.OrderResponseDto;
import com.marketplace.aggregatorservice.dto.ProductResponseDto;
import com.marketplace.aggregatorservice.dto.SellerResponseDto;
import com.marketplace.aggregatorservice.service.AggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aggregator")
public class AggregatorController {

    private final AggregatorService aggregatorService;

    @GetMapping("/{externalId}")
    public ProductResponseDto getProductDetails(@PathVariable("externalId") String externalId) {
        return aggregatorService.getProductDtoWithYourId(externalId);
    }

    @GetMapping("/head")
    public HttpHeaders getProductDetails() {
        return aggregatorService.getHttpHeaders();
    }

    @GetMapping("/post")
    public UUID getProduct() {
        return aggregatorService.postProduct();
    }

    @GetMapping("/orders/{externalId}")
    public OrderResponseDto getOrder(@PathVariable("externalId") String externalId) {
        return aggregatorService.getOrder(externalId);
    }

    @GetMapping("/sellers/{externalId}")
    public SellerResponseDto getSeller(@PathVariable("externalId") String externalId) {
        return aggregatorService.getSeller(externalId);
    }


    @GetMapping("/product/{externalId}")
    public UUID getProductExchange(@PathVariable("externalId") String externalId) {
        return aggregatorService.getProductExchange(externalId);
    }
}