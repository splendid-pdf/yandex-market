package com.yandex.market.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
public class ProductSellerServiceTest {

    private final MockMvc mockMvc;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    public void changePrice(UUID sellerId, UUID productId, long updatePrice, String path, ResultMatcher status) throws Exception {
        mockMvc.perform(patch(path, sellerId, productId)
                        .param("updatedPrice", String.valueOf(updatePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andReturn();
    }

    public ProductResponse findProductByExternalId(UUID externalId) {
        return productMapper.toResponseDto(
                productRepository
                        .findByExternalId(externalId)
                        .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId)));
    }

    public void executeDelete(UUID sellerId, List<UUID> productIds, String path) throws Exception {
        mockMvc.perform(delete(path, sellerId)
                        .content(objectMapper.writeValueAsString(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public long getActualCountAfterDelete(UUID sellerId) {
        return productService
                .getProductsBySellerId(sellerId, PageRequest.of(0, 20))
                .getTotalElements();
    }

    public long getArchivedCountBySellerId(UUID sellerId) {
        return productService
                .getArchivedProductsBySellerId(sellerId, PageRequest.of(0, 20))
                .getTotalElements();
    }

    public void moveFromToArchive(UUID sellerId, List<UUID> productIds, boolean isArchive, String path) throws Exception {
        mockMvc.perform(patch(path, sellerId)
                        .content(objectMapper.writeValueAsString(productIds))
                        .param("is-archive", String.valueOf(isArchive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void changeVisibility(UUID sellerId, List<UUID> productIds, boolean isVisible, String path) throws Exception {
        mockMvc.perform(patch(path, sellerId)
                        .content(objectMapper.writeValueAsString(productIds))
                        .param("is-visible", String.valueOf(isVisible))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
