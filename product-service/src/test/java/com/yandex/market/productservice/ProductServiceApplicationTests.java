package com.yandex.market.productservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SqlGroup({
        @Sql(value = "classpath:db/insert_tests_fields.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:db/delete_test_date.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ProductServiceApplicationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ProductService productService;

    @Value("${spring.app.seller.url}")
    private String PATH_TO_SELLER;

    @Test
    void findPageProductsBySellerId_successfulSearch() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f2");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{shopId}/products", sellerId, PageRequest.of(0, 20))
                        .param("method", "PRODUCT_LIST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductResponseDto> productsBySellerId = getPageOfProductFromMvcResult(mvcResult);

        long expectedTotalElements = 3;

        Assertions.assertNotNull(productsBySellerId);
        assertEquals(expectedTotalElements, productsBySellerId.getTotalElements());
    }

    @Test
    void findPageProductsBySellerId_noSellerFoundForCurrentId() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f9");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{shopId}/products", sellerId, PageRequest.of(0, 20))
                        .param("method", "PRODUCT_LIST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        boolean isEmpty = mvcResult.getResponse().getContentAsString().contains("\"empty\":true");

        Assertions.assertTrue(isEmpty);
    }

    @Test
    void findArchivePageOfProductsBySellerId_successfulSearch() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f2");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{shopId}/products", sellerId, PageRequest.of(0, 20))
                        .param("method", "ARCHIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductResponseDto> productsBySellerId = getPageOfProductFromMvcResult(mvcResult);

        long expectedTotalElements = 1;

        Assertions.assertNotNull(productsBySellerId);
        assertEquals(expectedTotalElements, productsBySellerId.getTotalElements());
    }

    @Test
    void deleteListProductBySellerId_successDeleted() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7071d1");

        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d1"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d2"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d3")));

        executeDelete(sellerId, productIds);

        long expectedBeforeDelete = 5 - productIds.size(),
                actualCountAfterDelete = getActualCountAfterDelete(sellerId);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    @Test
    void deleteListProductBySellerId_notAllProductsFound() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7071d1");

        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d4"),   // существует
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074c5"),   // не существует
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074c6"))); // не существует

        executeDelete(sellerId, productIds);

        int countNotFound = 2;
        long expectedBeforeDelete = 5 - (productIds.size() - countNotFound),
                actualCountAfterDelete = getActualCountAfterDelete(sellerId);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    @Test
    void deleteListProductBySellerId_notAllProductsCanBeDeleted() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7071d1");

        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d5"),   // isDeleted = false
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d6"),   // isDeleted = false
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d7"),   // isDeleted = true
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d8"))); // isDeleted = false


        executeDelete(sellerId, productIds);

        int countNotFound = 3;
        // ожидаемо 5 - (4 - 3 (isDeleted = false))
        long expectedBeforeDelete = 5 - (productIds.size() - countNotFound),
                actualCountAfterDelete = getActualCountAfterDelete(sellerId);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    @Test
    @Transactional
    public void create() throws Exception {
        UUID sellerExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        MvcResult mvcResult = mockMvc.perform(post("/public/api/v1/products/{sellerExternalId}/products", sellerExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateProductRequestDto.json"))))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualProductExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);

        Assertions.assertNotNull(productService.getProductByExternalId(actualProductExternalId));
    }

    @Test
    @Transactional
    public void createNegative() throws Exception {
        UUID sellerExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        mockMvc.perform(post("/public/api/v1/products/{sellerExternalId}/products", sellerExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateProductRequestDtoNegative.json"))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    private Page<ProductResponseDto> getPageOfProductFromMvcResult(MvcResult mvcResult) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductResponseDto>>() {
                });
    }

    private void executeDelete(UUID sellerId, List<UUID> productIds) throws Exception {
        mockMvc.perform(delete(
                        PATH_TO_SELLER + "{sellerId}/products", sellerId)
                        .content(objectMapper.writeValueAsString(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private long getActualCountAfterDelete(UUID sellerId) throws Exception {
        return objectMapper.readValue(mockMvc.perform(get(
                        PATH_TO_SELLER + "{shopId}/products", sellerId, PageRequest.of(0, 20))
                        .param("method", "ARCHIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductResponseDto>>() {
        }).getTotalElements();
    }

    public static class RestPageImpl<T> extends PageImpl<T> {

        @Serial
        private static final long serialVersionUID = 867755909294344407L;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public RestPageImpl(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {

            super(content, PageRequest.of(number, size), totalElements);
        }

        public RestPageImpl(List<T> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }

        public RestPageImpl(List<T> content) {
            super(content);
        }

        public RestPageImpl() {
            super(new ArrayList<>());
        }
    }
}