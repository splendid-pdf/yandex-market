package com.yandex.market.orderservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.orderservice.dto.OrderPreviewDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.model.OrderStatus;
import com.yandex.market.orderservice.model.PaymentType;
import com.yandex.market.orderservice.service.OrderService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testcontainers")
public class OrderControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    @Test
    @Transactional
    public void create() throws Exception {
        UUID userExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        MvcResult mvcResult = mockMvc.perform(post("/public/api/v1/users/{userId}/orders", userExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateOrderRequestDto.json"))))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualOrderExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);

        Assertions.assertNotNull(orderService.getOrderResponseDtoByExternalId(actualOrderExternalId));
    }

    @Test
    @Transactional
    public void createNegativeUserExternalId() throws Exception {
        UUID userExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        Path path = Path.of("src/test/resources/CreateOrderRequestDtoNegative.json");
        mockMvc.perform(post("/public/api/v1/users/{userId}/orders", userExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(path)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void getByExternalId() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/orders/{externalId}", orderExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OrderResponseDto orderResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDto.class);
        UUID expectedUserId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        UUID actualUserId = orderResponseDto.userId();

        Assertions.assertNotNull(orderResponseDto);
        Assertions.assertEquals(expectedUserId, actualUserId);
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void getByExternalIdNegative() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c5");
        mockMvc.perform(get("/public/api/v1/orders/{externalId}", orderExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void getOrderByUserId() throws Exception {
        UUID userExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/users/{userId}/orders", userExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Page<OrderPreviewDto> previewDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<OrderPreviewDto>>() {
        });

        UUID expectedOrderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        UUID actualOrderExternalId = previewDto.getContent().get(0).externalId();

        Assertions.assertEquals(previewDto.getTotalElements(), 1L);
        Assertions.assertEquals(previewDto.getTotalPages(), 1L);
        Assertions.assertEquals(expectedOrderExternalId, actualOrderExternalId);
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void getOrderByUserIdNegative() throws Exception {
        UUID userExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef0");
        mockMvc.perform(get("/public/api/v1/users/{userId}/orders", userExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void cancelOrder() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        mockMvc.perform(put("/public/api/v1/orders/{externalId}/cancellation", orderExternalId))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertEquals(OrderStatus.CANCELED, orderService.getOrderResponseDtoByExternalId(orderExternalId).orderStatus());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrderCompleted.sql")
    public void cancelOrderNegative() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        mockMvc.perform(put("/public/api/v1/orders/{externalId}/cancellation", orderExternalId))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void updateOrder() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        MvcResult mvcResult = mockMvc.perform(put("/public/api/v1/orders/{externalId}", orderExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateOrderRequestDto.json"))))
                .andExpect(status().isOk())
                .andReturn();

        OrderResponseDto actualOrderResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDto.class);
        String expectedStreet = "ul.Dobra";
        String actualStreet = actualOrderResponseDto.receiptMethod().getAddress().getStreet();

        Assertions.assertNotNull(orderService.getOrderResponseDtoByExternalId(orderExternalId));
        Assertions.assertNotNull(actualOrderResponseDto);
        Assertions.assertEquals(PaymentType.CARD_UPON_RECEIPT, actualOrderResponseDto.paymentType());
        Assertions.assertEquals(expectedStreet, actualStreet);
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void updateOrderNegative() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c5");
        mockMvc.perform(put("/public/api/v1/orders/{externalId}", orderExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateOrderRequestDto.json"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestOrderCompleted.sql")
    public void updateOrderNegativeStatusCompleted() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        MvcResult mvcResult = mockMvc.perform(put("/public/api/v1/orders/{externalId}", orderExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateOrderRequestDto.json"))))
                .andExpect(status().isBadRequest())
                .andReturn();
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