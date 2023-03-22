package com.yandex.market.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.service.ProductService;
import com.yandex.market.util.RestPageImpl;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Page<ProductResponseDto> productsBySellerId = getPageOfProductFromMvcResult(mvcResult);

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

        Page<ProductResponseDto> productsBySellerId = getPageOfProductFromMvcResult(mvcResult);

        long expectedTotalElements = 1;

        Assertions.assertNotNull(productsBySellerId);
        assertEquals(expectedTotalElements, productsBySellerId.getTotalElements());
    }

    @Test
    @Disabled
    void shouldDeleteListProductBySellerIdSuccessDeleted() throws Exception {
        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d1"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d2"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d3")));

        executeDelete(DELETE_SELLER_ID, productIds);

        long expectedBeforeDelete = 5 - productIds.size(),
                actualCountAfterDelete = getActualCountAfterDelete(DELETE_SELLER_ID);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    /**
     * Method: shouldDeleteListProductBySellerIdNotAllProductsFound()
     *
     * @throws Exception throws out methods {@link ProductServiceApplicationTests#executeDelete(UUID, List)},
     *                   {@link ProductServiceApplicationTests#getActualCountAfterDelete(UUID)}
     *                   <br>
     * @value <b>productIds</b> list elements (0 - exists, 1 - not exists, 2 - not exists)
     */
    @Test
    @Disabled
    void shouldDeleteListProductBySellerIdNotAllProductsFound() throws Exception {
        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d4"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074c5"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074c6")));

        executeDelete(DELETE_SELLER_ID, productIds);

        int countNotFound = 2;
        long expectedBeforeDelete = 5 - (productIds.size() - countNotFound),
                actualCountAfterDelete = getActualCountAfterDelete(DELETE_SELLER_ID);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
    }

    /**
     * Method: shouldDeleteListProductBySellerIdNotAllProductsCanBeDeleted()
     *
     * @throws Exception throws out methods {@link ProductServiceApplicationTests#executeDelete(UUID, List)},
     *                   {@link ProductServiceApplicationTests#getActualCountAfterDelete(UUID)}
     *                   <br>
     * @value <b>productIds</b> list elements (isArchived = false, isArchived = false,
     * 2 - isArchived = true, 3 - isArchived = false)
     * @value <b>expectedBeforeDelete</b> expected 5 - (4 - 3 (isArchived = false))
     */
    @Test
    @Disabled
    void shouldDeleteListProductBySellerIdNotAllProductsCanBeDeleted() throws Exception {
        List<UUID> productIds = new ArrayList<>(List.of(
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d5"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d6"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d7"),
                UUID.fromString("301c5370-be41-421e-9b15-f1e80a7074d8")));

        executeDelete(DELETE_SELLER_ID, productIds);

        int countNotFound = 3;

        long expectedBeforeDelete = 5 - (productIds.size() - countNotFound),
                actualCountAfterDelete = getActualCountAfterDelete(DELETE_SELLER_ID);

        assertEquals(expectedBeforeDelete, actualCountAfterDelete);
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

        Assertions.assertNotNull(productService.getProductByExternalId(actualProductExternalId, null));
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

        ProductResponseDto actualProductResponseDto =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductResponseDto.class);

        assertNotNull(actualProductResponseDto);
        assertEquals(productExternalId, actualProductResponseDto.externalId());
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

    @Test
    @Transactional
    void shouldChangeProductPriceSellerAndProductFoundAndChanged() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c60");
        UUID productId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c61");
        long newPrice = 30L;

        changePrice(sellerId, productId, newPrice, status().isNoContent());

        Long actualPrice = findProductByExternalId(productId).price();
        assertEquals(newPrice, actualPrice, "Received price not as expected");
    }

    @Test
    @Transactional
    void shouldChangeProductPriceSellerNotFoundAndNotChanged() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c63");
        UUID productId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c61");
        long newPrice = 40L;

        changePrice(sellerId, productId, newPrice, status().isNoContent());

        long actualPrice = findProductByExternalId(productId).price();
        assertNotEquals(newPrice, actualPrice, "Received values are equal, which is not expected");
    }

    @Test
    @Transactional
    void shouldChangeProductPriceProductNotFoundAndNotChanged() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c60");
        UUID productId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c69");
        long newPrice = 50L;

        changePrice(sellerId, productId, newPrice, status().isNoContent());

        assertThrows(EntityNotFoundException.class, () -> findProductByExternalId(productId),
                "Product was found by given externalId");
    }

    private void changePrice(UUID sellerId, UUID productId, long newPrice, ResultMatcher status) throws Exception {
        mockMvc.perform(patch(
                        PATH_TO_SELLER + "{sellerId}/products/price/{productId}", sellerId, productId)
                        .param("newPrice", String.valueOf(newPrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andReturn();
    }

    private ProductResponseDto findProductByExternalId(UUID externalId) {
        return productMapper.toResponseDto(
                productRepository
                        .findByExternalId(externalId)
                        .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId)));
    }

    private Page<ProductResponseDto> getPageOfProductFromMvcResult(MvcResult mvcResult)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductResponseDto>>() {
                });
    }

    private void executeDelete(UUID sellerId, List<UUID> productIds) throws Exception {
        mockMvc.perform(delete(
                        PATH_TO_SELLER + "{sellerId}/products", sellerId)
                        .content(objectMapper.writeValueAsString(productIds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private long getActualCountAfterDelete(UUID sellerId) throws Exception {
        return objectMapper.readValue(mockMvc.perform(get(
                        PATH_TO_SELLER + "{sellerId}/products", sellerId, PageRequest.of(0, 20))
                        .param("method", "ARCHIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductResponseDto>>() {
        }).getTotalElements();
    }
}