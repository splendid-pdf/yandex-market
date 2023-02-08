package com.yandex.market.favoritesservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.favoritesservice.dto.FavoritesRequestDto;
import com.yandex.market.favoritesservice.dto.FavoritesResponseDto;
import com.yandex.market.favoritesservice.repository.FavoritesRepository;
import com.yandex.market.util.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class FavoritesServiceApplicationTests {

    @Value("${spring.application.url}")
    private String PUBLIC_API;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final FavoritesRepository repository;

    private final UUID userId = UUID.fromString("5728bd51-996c-4ddf-a97d-57855203450d");

    private final UUID productId = UUID.fromString("5728bd51-996c-4ddf-a97d-57355203720d");

    @Test
    @Transactional
    public void createFavorites_returnFavoritesExternalIdAnd201Created() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(PUBLIC_API + "/{userId}/favorites", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jsonToFavoritesRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertNotNull(
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class));
        Assertions.assertNotNull(repository.findById(1L).orElse(null));
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void getFavoritesByUserId_returnPageFavoritesAnd200_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PUBLIC_API + "/{userId}/favorites", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Page<FavoritesResponseDto> page =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<RestPageImpl<FavoritesResponseDto>>() {
                        });

        UUID actualProductId = page.getContent().get(0).productId();

        Assertions.assertNotNull(page);
        Assertions.assertEquals(page.getTotalElements(), 1L);
        Assertions.assertEquals(page.getTotalPages(), 1L);
        Assertions.assertEquals(productId, actualProductId);
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void deleteFavorites_return204NoContent() throws Exception {
        mockMvc.perform(delete(PUBLIC_API + "/{userId}/favorites/{productId}", userId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
        Assertions.assertNull(repository.findById(1L).orElse(null));
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void deleteFavoritesNegative_return404NotFound() throws Exception {
        mockMvc.perform(delete(PUBLIC_API + "/{userId}/favorites/{productId}", userId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void getFavoritesByUserIdNegative_returnPageFavoritesAnd200_Ok() throws Exception {
        mockMvc.perform(get(PUBLIC_API + "/{userId}/favorites", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    private FavoritesRequestDto jsonToFavoritesRequestDto() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/json/CreateFavoritesRequestDto.json"));
        return objectMapper.readValue(json, FavoritesRequestDto.class);
    }
}