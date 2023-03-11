package com.yandex.market.favoritesservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import com.yandex.market.favoritesservice.repository.FavoriteItemRepository;
import com.yandex.market.util.RestPageImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FavoriteItemServiceApplicationTests {

    private final static String USER_ID = "9fb57cbe-a232-4b33-beb0-0e0d8a9d9e20";
    private final static String PRODUCT_ID = "5d61ef1a-b76a-4675-b791-356ab3b834d5";
    private final static String FAVORITE_ID = "e5c6b86d-d167-4f40-a5d0-d1f7e66c5776";
    private final static UUID USER_ID_UUID = UUID.fromString(USER_ID);
    private final static UUID PRODUCT_ID_UUID = UUID.fromString(PRODUCT_ID);
    private final static UUID FAVORITE_ID_UUID = UUID.fromString(FAVORITE_ID);
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FavoriteItemRepository repository;

    @Value("${spring.application.url}")
    private String PUBLIC_API;

    @Value("classpath:json/expected-response-of-added-favorite-item.json")
    private Resource expectedAddedFavoriteItemResource;

    @Test
    @Transactional
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createFavorites_returnFavoritesExternalIdAnd201Created() throws Exception {
        try (MockedStatic<UUID> mockedStatic = Mockito.mockStatic(UUID.class)) {
            mockedStatic.when(UUID::randomUUID).thenReturn(FAVORITE_ID_UUID);
            mockedStatic.when(() -> UUID.fromString(PRODUCT_ID)).thenReturn(PRODUCT_ID_UUID);
            mockedStatic.when(() -> UUID.fromString(USER_ID)).thenReturn(USER_ID_UUID);

            MvcResult mvcResult = mockMvc.perform(
                            post(PUBLIC_API + "{userId}/favorites/{productId}", USER_ID, PRODUCT_ID))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString();
            String expectedUUID = unwrapString(content);
            FavoriteItem favorite = repository.findById(1L)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

            assertThat(expectedUUID).isEqualTo(FAVORITE_ID);
            assertThat(favorite)
                    .usingRecursiveComparison()
                    .ignoringFields("addedAt")
                    .isEqualTo(objectMapper.readValue(
                            Files.readString(Path.of(expectedAddedFavoriteItemResource.getURI())), FavoriteItem.class));
        }
    }

    @Test
    @Transactional
    @Sql("/db/insert-favorites-query.sql")
    public void getFavoritesByUserId_returnPageFavoritesAnd200_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PUBLIC_API + "/{userId}/favorites", USER_ID).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Page<FavoriteItemResponseDto> page = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<RestPageImpl<FavoriteItemResponseDto>>() {
                });

        UUID actualFavoritesId = page.getContent().get(0).externalId();
        UUID actualProductId = page.getContent().get(0).productId();
        UUID actualUserId = page.getContent().get(0).userId();

        assertNotNull(page);
        assertEquals(page.getTotalElements(), 1L);
        assertEquals(page.getTotalPages(), 1);
        assertEquals(FAVORITE_ID_UUID, actualFavoritesId);
        assertEquals(PRODUCT_ID_UUID, actualProductId);
        assertEquals(USER_ID_UUID, actualUserId);
    }

    @Test
    @Transactional
    @Sql("/db/insert-favorites-query.sql")
    public void deleteFavorites_return204NoContent() throws Exception {
        mockMvc.perform(
                        delete(PUBLIC_API + "/{userId}/favorites/{productId}", USER_ID, PRODUCT_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    @Transactional
    public void deleteFavoritesNegative_return404NotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete(PUBLIC_API + "/{userId}/favorites/{productId}", USER_ID, PRODUCT_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        EntityNotFoundException entityNotFoundException =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EntityNotFoundException.class);
        assertNotNull(entityNotFoundException);
    }

    @Test
    @Transactional
    public void getFavoritesByUserIdNegative_returnPageFavoritesAnd200_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PUBLIC_API + "/{userId}/favorites", USER_ID).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThrows(ValueInstantiationException.class, () -> objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        new TypeReference<RestPageImpl<FavoriteItemResponseDto>>() {
                        }),
                "Page size must not be less than one"
        );
    }

    private String unwrapString(String str) {
        return str.substring(1, str.length() - 1);
    }
}