package com.yandex.market.productservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.util.RestPageImpl;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MvcResult;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
public class ProductSellerServiceTest {
    private final MockMvc mockMvc;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    private final String AUTH_TOKEN = "eyJraWQiOiI1YWIwODM5Mi00ZjUzLTQ5ZWUtODM1My0xZjZiZTIwODg0MGIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzZWxsZXJfZGltYUBnbWFpbC5ydSIsImF1ZCI6ImNsaWVudCIsIm5iZiI6MTY4MjE1MjU5OSwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly81MS4yNTAuMTAyLjEyOjkwMDAiLCJleHAiOjE2ODI3NTczOTksImlhdCI6MTY4MjE1MjU5OSwic2VsbGVyLWlkIjoiY2IwNDFkMzEtYTM0NS00ZDgwLTk3MWEtNzBjNDljYmM1YzI4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9TRUxMRVIiXX0.I1PH6PxdOfkZENw2UmPHlUaRIxPS_4ApOG04z2W23um0Oyb68X4v1gFn19m3SOb677g-Ofkrdy1fJ_BEZLYYSs_VeLotVv1FgRFxRe2L2WO-phXMIlPBuq0XGwxGGtdPq9dXQMjqjeHa6rlRlAfMLB8zAom7Y8NFBGELgAfZGScLfmP1iERQ-vD3_OwnqK7rKBCRayJZmWCUzR0geLAOzjl22dV69RtbwdAZFCj4VyupwjQZxDcKyN9G3w-6D-Fs3pyallqwoz1L_MfCz7RuRan64H1M9lCXOItYfQXLpqDKpu3yKg4dt9mhjzdC5iH0VE0Uee9IXj2m-AFqnKHSvg";

    public void changePrice(String path, UUID sellerId, UUID productId, long updatePrice) throws Exception {
        mockMvc.perform(patch(path, sellerId, productId)
                        .param("price", String.valueOf(updatePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void changeCount(UUID sellerId, UUID productId, long updateCount, String path) throws Exception {
        mockMvc.perform(patch(path, sellerId, productId)
                        .param("updated-count", String.valueOf(updateCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

    public void changePriceForProduct(String path, UUID sellerId, UUID productId, long updatePrice) throws Exception {
        mockMvc.perform(patch(path, sellerId, productId)
                        .header("Authorization", "Bearer " + AUTH_TOKEN)
                        .param("price", String.valueOf(updatePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public Page<SellerProductPreview> getProducts(String path, UUID sellerId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path, sellerId, PageRequest.of(0, 20))
                        .header("Authorization", "Bearer " + AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return getPageOfProductFromMvcResult(mvcResult);
    }

    public Page<SellerProductPreview> getArchivedProducts(String path, UUID sellerId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path, sellerId, PageRequest.of(0, 20))
                        .header("Authorization", "Bearer " + AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return getPageOfProductFromMvcResult(mvcResult);
    }

    private Page<SellerProductPreview> getPageOfProductFromMvcResult(MvcResult mvcResult)
            throws IOException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<SellerProductPreview>>() {
        });
    }
}
