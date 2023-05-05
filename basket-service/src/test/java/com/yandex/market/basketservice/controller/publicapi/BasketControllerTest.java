package com.yandex.market.basketservice.controller.publicapi;

import com.yandex.market.basketservice.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.yandex.market.auth.util.AuthUtils.token;
import static com.yandex.market.basketservice.models.Environment.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BasketControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Получение информации о содержании корзины авторизованным пользователем")
    void whenUserWithTokenRequestInformationAboutBasket_thenOk() throws Exception {
        mockMvc.perform(
                        get(BASKET_PATH, USER_ID_3, PageRequest.of(0, 5))
                                .with(authentication(token(USER_ID_3, "ROLE_USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(Files.readString(Path.of(PATH_BASKET_RESPONSE))));
    }

    @Test
    @DisplayName("Попытка получения информации о корзине у неавторизованного пользователя")
    void whenUserWithoutTokenTryToGetInformationAboutBasket_thenExpectUnauthorizedStatus() throws Exception {
        mockMvc.perform(
                        get(BASKET_PATH, USER_ID_2, PageRequest.of(0, 5))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Авторизованный пользователь добавляет существующий товар в корзину")
    void whenUserWithTokenTryToAddExistItemToBasket_thenOk() throws Exception {
        mockMvc.perform(
                        patch(BASKET_PATH, USER_ID_2)
                                .with(authentication(token(USER_ID_2, "ROLE_USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productId" : "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091",
                                            "numberOfItems" : "7"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("25"));
    }

    @Test
    @DisplayName("Авторизованный пользователь пытается добавить существующий товар с отрицательным количеством в корзину")
    void whenUserWithTokenTryToAddExistItemWithNegativeNumberOfItemsToBasket_thenBadRequest() throws Exception {
        mockMvc.perform(
                        patch(BASKET_PATH, USER_ID_2)
                                .with(authentication(token(USER_ID_2, "ROLE_USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productId" : "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091",
                                            "numberOfItems" : "-10"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Авторизованный пользователь пытается добавить несуществующий товар в корзину")
    void whenUserWithTokenTryToAddNotExistItemToBasket_thenBadRequest() throws Exception {
        mockMvc.perform(
                        patch(BASKET_PATH, USER_ID_2)
                                .with(authentication(token(USER_ID_2, "ROLE_USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productId" : "f34c4cd3-6fe7-4d3e-b82c-f5d044e45555",
                                            "numberOfItems" : "10"
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Авторизованный пользователь удаляет товар из корзины")
    void whenUserWithTokenTryToRemoveItemFromBasket_thenOk() throws Exception {
        mockMvc.perform(
                        delete(BASKET_PATH, USER_ID_2)
                                .with(authentication(token(USER_ID_2, "ROLE_USER")))
                                .param("products", "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091"))
                .andExpect(status().isOk())
                .andExpect(content().string("18"));
    }

    @Test
    @DisplayName("Авторизованный пользователь без корзины добавляет товар")
    void whenUserWithTokenButWithoutBasketTryToAddItemToBasket_thenOk() throws Exception {
        mockMvc.perform(
                        patch(BASKET_PATH, USER_ID_1)
                                .with(authentication(token(USER_ID_1, "ROLE_USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "productId" : "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091",
                                            "numberOfItems" : "7"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }
}