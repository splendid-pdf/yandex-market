package com.yandex.market.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.models.ArchiveTest;
import com.yandex.market.productservice.models.ProductTest;
import com.yandex.market.productservice.models.ProductsTest;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.util.RestPageImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
public class ProductTestService {
    private final MockMvc mockMvc;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    private final String AUTH_TOKEN = "eyJraWQiOiI1YWIwODM5Mi00ZjUzLTQ5ZWUtODM1My0xZjZiZTIwODg0MGIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzZWxsZXJfZGltYUBnbWFpbC5ydSIsImF1ZCI6ImNsaWVudCIsIm5iZiI6MTY4MjE1MjU5OSwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly81MS4yNTAuMTAyLjEyOjkwMDAiLCJleHAiOjE2ODI3NTczOTksImlhdCI6MTY4MjE1MjU5OSwic2VsbGVyLWlkIjoiY2IwNDFkMzEtYTM0NS00ZDgwLTk3MWEtNzBjNDljYmM1YzI4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9TRUxMRVIiXX0.I1PH6PxdOfkZENw2UmPHlUaRIxPS_4ApOG04z2W23um0Oyb68X4v1gFn19m3SOb677g-Ofkrdy1fJ_BEZLYYSs_VeLotVv1FgRFxRe2L2WO-phXMIlPBuq0XGwxGGtdPq9dXQMjqjeHa6rlRlAfMLB8zAom7Y8NFBGELgAfZGScLfmP1iERQ-vD3_OwnqK7rKBCRayJZmWCUzR0geLAOzjl22dV69RtbwdAZFCj4VyupwjQZxDcKyN9G3w-6D-Fs3pyallqwoz1L_MfCz7RuRan64H1M9lCXOItYfQXLpqDKpu3yKg4dt9mhjzdC5iH0VE0Uee9IXj2m-AFqnKHSvg";

    private final String HEADER = "Bearer " + AUTH_TOKEN;

    public UUID createProduct(String path, UUID sellerId, String bodyRequest, ResultMatcher status)
            throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(path, sellerId)
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status)
                .andReturn();

        if (mvcResult.getResponse().getStatus() == 201) {
            return getUuid(mvcResult);
        }
        return null;
    }

    public ProductResponse updateProduct(String path, UUID sellerId, UUID productId, String updateProduct, ResultMatcher status)
            throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(path, sellerId, productId)
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProduct))
                .andExpect(status)
                .andReturn();
        return getProductFromMvcResult(mvcResult, new TypeReference<>() {
        });
    }

    public Page<ProductsTest> getProducts(String path, UUID sellerId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path, sellerId, PageRequest.of(0, 20))
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return getProductFromMvcResult(mvcResult, new TypeReference<RestPageImpl<ProductsTest>>() {
        });
    }

    public Page<ArchiveTest> getArchivedProducts(String path, UUID sellerId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path, sellerId, PageRequest.of(0, 20))
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return getProductFromMvcResult(mvcResult, new TypeReference<RestPageImpl<ArchiveTest>>() {
        });
    }

    public ProductTest getProduct(String path, UUID sellerId, UUID productId, ResultMatcher status) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path, sellerId, productId, PageRequest.of(0, 20))
                        .header("Authorization", HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andReturn();

        if (mvcResult.getResponse().getStatus() == 200) {
            return getProductFromMvcResult(mvcResult, new TypeReference<>() {
            });
        }
        return null;
    }

    public void changeArchive(String path, UUID sellerId, List<UUID> productIds, boolean isArchive)
            throws Exception {
        mockMvc.perform(patch(path, sellerId)
                        .header("Authorization", HEADER)
                        .param("is-archive", String.valueOf(isArchive))
                        .param("product-ids", convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public void changeVisible(String path, UUID sellerId, List<UUID> productIds, boolean isVisible)
            throws Exception {
        mockMvc.perform(patch(path, sellerId)
                        .header("Authorization", HEADER)
                        .param("is-visible", String.valueOf(isVisible))
                        .param("product-ids", convertListToParam(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public void changeProductParameters(String path, UUID sellerId, UUID productId,
                                        String characteristicName, String characteristicValue) throws Exception {
        mockMvc.perform(patch(path, sellerId, productId)
                        .header("Authorization", HEADER)
                        .param(characteristicName, characteristicValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public Product getProduct(UUID productId) {
        return productRepository.findByExternalId(productId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Product id = " + productId + " not found")
                );
    }

    private String convertListToParam(List<UUID> productIds) {
        return productIds.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));
    }

    private String getNormalEncoding(MvcResult result) {
        return new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private <T> T getProductFromMvcResult(MvcResult mvcResult, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(getNormalEncoding(mvcResult), typeReference);
    }

    private UUID getUuid(MvcResult mvcResult) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);
    }
}
