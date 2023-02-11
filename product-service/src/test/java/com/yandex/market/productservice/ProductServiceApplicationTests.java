package com.yandex.market.productservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.response.ProductFullInfoResponse;
import com.yandex.market.productservice.dto.response.ProductPriceResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Sql(
        value = {"classpath:db/insert_tests_fields.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
        value = {"classpath:db/delete_test_date.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductServiceApplicationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final String PATH_TO_MAIN = "/public/api/v1/";
    private final String PATH_TO_SHOP = "/public/api/v1/shops/";
    private final String PATH_TO_BRANCH = "/public/api/v1/branches/";

    @Test
    @Transactional
    void getByExternalIdCorrect() throws Exception {
        UUID productPriceExternalId = UUID.fromString("f1ea093c-daa2-43a4-b61c-fb8596034e70");

        MvcResult mvcResult = mockMvc.perform(get(PATH_TO_MAIN + "{externalId}", productPriceExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductPriceResponseDto responseDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ProductPriceResponseDto.class);

        UUID expectedBranchId = UUID.fromString("9f1d3971-9106-40b5-9b18-2014c0b703b0"),
                expectedShopId = UUID.fromString("caa2a172-5f4c-4224-95ce-c79a89684e90"),
                actualBranchId = responseDto.branchId(),
                actualShopSystemId = responseDto.shopSystemId();

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedBranchId, actualBranchId);
        Assertions.assertEquals(expectedShopId, actualShopSystemId);
    }

    @Test
    @Transactional
    void getByExternalIdIncorrect() throws Exception {
        UUID productPriceExternalId = UUID.fromString("f1ea093c-daa2-43a4-b61c-fb8596034e79");

        mockMvc.perform(get(PATH_TO_MAIN + "{externalId}", productPriceExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    void getPageByShopIdCorrect() throws Exception {
        UUID shopId = UUID.fromString("caa2a172-5f4c-4224-95ce-c79a89684e90");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_SHOP + "{shopId}/products", shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductFullInfoResponse> productFullInfoResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductFullInfoResponse>>() {
                });

        Assertions.assertNotNull(productFullInfoResponses);
        Assertions.assertEquals(2, productFullInfoResponses.getTotalElements());
    }


    @Test
    @Transactional
    void getPageByBranchIdCorrect() throws Exception {
        UUID branchId = UUID.fromString("9f1d3971-9106-40b5-9b18-2014c0b703b1");

        MvcResult mvcResult = mockMvc.perform(get(
                        PATH_TO_BRANCH + "{branchId}/products", branchId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ProductFullInfoResponse> productFullInfoResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ProductFullInfoResponse>>() {
                });

        Assertions.assertNotNull(productFullInfoResponses);
        Assertions.assertEquals(1, productFullInfoResponses.getTotalElements());
    }


    public static class RestPageImpl<T> extends PageImpl<T> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public RestPageImpl(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements) {

            super(content, PageRequest.of(number, size), totalElements);
        }
    }
}
