package com.yandex.market.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTestService {

    private final ObjectMapper objectMapper;

    private final ProductRepository productRepository;

    public Product getProduct(UUID productId) {
        return productRepository.findByExternalId(productId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Product id = " + productId + " not found")
                );
    }

    public String convertListToParam(List<UUID> productIds) {
        return productIds.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));
    }

    public String getNormalEncoding(MvcResult result) {
        return new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    public  <T> T getProductFromMvcResult(MvcResult mvcResult, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(getNormalEncoding(mvcResult), typeReference);
    }

    public UUID getUuid(MvcResult mvcResult) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);
    }
}
