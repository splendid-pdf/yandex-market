package com.yandex.market.basketservice.controller.publicapi;


import com.yandex.market.basketservice.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.yandex.market.basketservice.models.Environment.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BasketControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenUserWithTokenRequestInformationAboutBasket_thenOk() throws Exception {
        mockMvc.perform(
                        get(BASKET_PATH, REAL_USER_ID, PageRequest.of(0, 10))
//                                .with(authentication(token(REAL_USER_ID,"ROLE_USER")))
                                .header("Authorization", AUTH_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}