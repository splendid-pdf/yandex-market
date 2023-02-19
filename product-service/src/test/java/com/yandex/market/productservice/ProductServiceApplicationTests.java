package com.yandex.market.productservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Value("${spring.app.product.url}")
    private String PATH_TO_PRODUCTS;


    @Test
    void findPageProductsBySellerId_successfulSearch() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f2");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{shopId}/products", sellerId, PageRequest.of(0, 20))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductResponseDto> productsBySellerId = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductResponseDto>>() {
                });

        long expectedTotalElements = 3;

        Assertions.assertNotNull(productsBySellerId);
        Assertions.assertEquals(expectedTotalElements, productsBySellerId.getTotalElements());
    }


    @Test
    void findPageProductsBySellerId_noSellerFoundForCurrentId() throws Exception {
        UUID sellerId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f9");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SELLER + "{shopId}/products", sellerId, PageRequest.of(0, 20))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        boolean isEmpty = mvcResult.getResponse().getContentAsString().contains("\"empty\":true");

        Assertions.assertTrue(isEmpty);
    }

    @Test
    void deleteProductByExternalId_userFoundAndSuccessfullyDeleted() throws Exception {
        UUID externalId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f5");
        // isDeleted = false, isVisible = true
        mockMvc.perform(delete(PATH_TO_PRODUCTS + "/{externalId}", externalId))
                .andDo(print())
                .andExpect(status().isOk());

        ProductResponseDto product = productService.getProductNoLimitsByExternalId(externalId);

        Assertions.assertNotNull(product);
        Assertions.assertTrue(product.isDeleted());
        Assertions.assertFalse(product.isVisible());
    }

    @Test
    void deleteProductByExternalId_userIsNotFound() throws Exception {
        UUID externalId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f9");
        mockMvc.perform(delete(PATH_TO_PRODUCTS + "/{externalId}", externalId))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteProductByExternalId_userFoundAndButHasAlreadyBeenDeleted() throws Exception {
        UUID externalId = UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074f7");
        // isDeleted = true, isVisible = false
        mockMvc.perform(delete(PATH_TO_PRODUCTS + "/{externalId}", externalId))
                .andDo(print())
                .andExpect(status().isOk());

        ProductResponseDto product = productService.getProductNoLimitsByExternalId(externalId);

        Assertions.assertNotNull(product);
        Assertions.assertTrue(product.isDeleted());
        Assertions.assertFalse(product.isVisible());
    }

    static class RestPageImpl<T> extends PageImpl<T> {

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

