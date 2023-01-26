package com.yandex.market.orderservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class OrderControllerTest {

    private final MockMvc mockMvc;
    private final OrderService orderService;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());


    @Test
    @Transactional
    @Sql
    public void create() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/users/{userId}/orders"))
                .andExpect(status().isOk())
                .andReturn();

    OrderResponseDto actualOrder = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
    });

        Assertions.assertNotNull(actualOrder);
        Assertions.assertNotNull(orderService.getByExternalId(actualOrder.externalId()));

    }

    @Test
    public void getByExternalId(){

    }

    @Test
    public void getOrderByUserId(){

    }

     @Test
    public void cancelOrder(){

    }

      @Test
    public void updateOrder(){

    }


}
