package com.yandex.market.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.service.ProductService;
import com.yandex.market.util.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SqlGroup({
        @Sql(value = "classpath:db/insert_tests_fields.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:db/insert_types.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
class ProductServiceApplicationTests {

    private final static UUID DELETE_SELLER_ID = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7071d1");
    private final static UUID FIND_SELLER_ID = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f2");
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Value("${spring.app.seller.url}")
    private String PATH_TO_SELLER;

    @Value("${spring.app.product.url}")
    private String PATH_TO_PRODUCT;

    @Test
    @Disabled
    void shouldFindPageProductsBySellerIdSuccessfulSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{sellerId}/products",
                        FIND_SELLER_ID, PageRequest.of(0, 20))
                        .param("method", "PRODUCT_LIST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductResponse> productsBySellerId = getPageOfProductFromMvcResult(mvcResult);

        long expectedTotalElements = 3;

        Assertions.assertNotNull(productsBySellerId);
        assertEquals(expectedTotalElements, productsBySellerId.getTotalElements());
    }

    @Test
    void shouldFindPageProductsBySellerIdNoSellerFoundForCurrentId() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f9");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{sellerId}/products", sellerId, PageRequest.of(0, 20))
                        .param("method", "PRODUCT_LIST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        boolean isEmpty = mvcResult.getResponse().getContentAsString().contains("\"empty\":true");

        Assertions.assertTrue(isEmpty);
    }

    @Test
    @Disabled
    void shouldFindArchivePageOfProductsBySellerIdSuccessfulSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{sellerId}/products", FIND_SELLER_ID, PageRequest.of(0, 20))
                        .param("method", "ARCHIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductResponse> productsBySellerId = getPageOfProductFromMvcResult(mvcResult);

        long expectedTotalElements = 1;

        Assertions.assertNotNull(productsBySellerId);
        assertEquals(expectedTotalElements, productsBySellerId.getTotalElements());
    }

    @Test
    @Disabled
    @Transactional
    void shouldCreateProductReturnExternalIdAndStatus201Created() throws Exception {
        UUID sellerExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");

        MvcResult mvcResult = mockMvc.perform(
                        post(PATH_TO_PRODUCT + "{sellerId}/products", sellerExternalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(Files.readString(Path.of("src/test/resources/CreateProductRequestDto.json"))))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualProductExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);

        Assertions.assertNotNull(productService.getProductByExternalId(actualProductExternalId));
    }

    @Test
    @Transactional
    void shouldCreateNegative() throws Exception {
        UUID sellerExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        mockMvc.perform(post(PATH_TO_PRODUCT + "{sellerId}/products", sellerExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateProductRequestDtoNegative.json"))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Disabled
    @Transactional
    void shouldGetProductByExternalIdReturnProductResponseDtoAndStatus200Ok() throws Exception {
        UUID productExternalId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f5");

        MvcResult mvcResult = mockMvc.perform(get(PATH_TO_PRODUCT + "{externalId}", productExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse actualProductResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductResponse.class);

        assertNotNull(actualProductResponse);
        assertEquals(productExternalId, actualProductResponse.id());
    }

    @Test
    @Transactional
    void shouldGetProductByExternalIdNegativeReturnProductResponseDtoAndStatus200Ok() throws Exception {
        UUID productExternalId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7124f5");

        MvcResult mvcResult = mockMvc.perform(get(PATH_TO_PRODUCT + "{externalId}", productExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class));
    }

    private Page<ProductResponse> getPageOfProductFromMvcResult(MvcResult mvcResult)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductResponse>>() {
                });
    }
}