package com.yandex.market.favoritesservice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class FavoritesServiceApplicationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private static final String PUBLIC_API = "/public/api/v1/users/";

    @Test
    public void createFavoritesTest() throws Exception {
        UUID userId = UUID.fromString("5728bd51-996c-4ddf-a97d-57855203450d");

        MvcResult mvcResult = mockMvc.perform(post(PUBLIC_API + "{userId}/favorites", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(
                                Path.of("src/test/resources/CreateFavoritesRequestDto.json"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertNotNull(
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class));
    }
}