package com.yandex.market.orderservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.orderservice.dto.OrderResponsePreview;
import com.yandex.market.orderservice.dto.OrderResponse;
import com.yandex.market.orderservice.model.OrderStatus;
import com.yandex.market.orderservice.model.PaymentType;
import com.yandex.market.orderservice.service.OrderService;
import com.yandex.market.util.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testcontainers")
class OrderControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    @Test
    @Transactional
    public void create() throws Exception {
        UUID userExternalId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        MvcResult mvcResult = mockMvc.perform(
                post("/public/api/v1/users/{userId}/orders", userExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateOrderRequestDto.json"))))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualOrderExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                UUID.class);

        Assertions.assertNotNull(orderService.getOrderResponseById(actualOrderExternalId));
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

        OrderResponse orderResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), OrderResponse.class);
        UUID expectedUserId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");
        UUID actualUserId = orderResponse.userId();

        Assertions.assertNotNull(orderResponse);
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
        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/users/{userId}/orders/previews", userExternalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Page<OrderResponsePreview> previewDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<RestPageImpl<OrderResponsePreview>>() {
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
        mockMvc.perform(get("/public/api/v1/users/{userId}/orders/previews", userExternalId)
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

        Assertions.assertEquals(OrderStatus.CANCELED,
                orderService.getOrderResponseById(orderExternalId).orderStatus());
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

//    @Test
//    @Transactional
//    public void getOrderPreviewsBySellerId(){
//        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
//        mockMvc.perform(get("/public/api/v1/sellers/{sellerId}/orders", sellerId))
//                .andExpect()
//    }


/*    @Test
    @Transactional
    @Sql("/db/insertTestOrder.sql")
    public void updateOrder() throws Exception {
        UUID orderExternalId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
        MvcResult mvcResult = mockMvc.perform(put("/public/api/v1/orders/{externalId}", orderExternalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateOrderRequestDto.json"))))
                .andExpect(status().isOk())
                .andReturn();

        OrderResponse actualOrderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);
        String expectedStreet = "ul.Dobra";
        String actualStreet = actualOrderResponse.receiptMethod().getAddress().getStreet();

        Assertions.assertNotNull(orderService.getOrderResponseById(orderExternalId));
        Assertions.assertNotNull(actualOrderResponse);
        Assertions.assertEquals(PaymentType.CARD_UPON_RECEIPT, actualOrderResponse.paymentType());
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
    }*/
}