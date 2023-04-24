package com.yandex.market.favoritesservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import com.yandex.market.favoritesservice.repository.FavoriteItemRepository;
import com.yandex.market.util.RestPageImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SqlGroup({
        @Sql(
                scripts = "classpath:db/truncate_favorites.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
class FavoriteItemServiceApplicationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final FavoriteItemRepository repository;

    private final static String USER_ID = "6a2e63a7-a8b7-4a5e-9422-6a16ee963e8d";
    private final static String PRODUCT_ID = "5d61ef1a-b76a-4675-b791-356ab3b834d5";
    private final static String FAVORITE_ID = "e5c6b86d-d167-4f40-a5d0-d1f7e66c5776";
    private final static UUID USER_ID_UUID = UUID.fromString(USER_ID);
    private final static UUID PRODUCT_ID_UUID = UUID.fromString(PRODUCT_ID);
    private final static UUID FAVORITE_ID_UUID = UUID.fromString(FAVORITE_ID);

    @Value("${spring.application.url}")
    private String PUBLIC_API;

    @Value("classpath:json/expected-response-of-added-favorite-item.json")
    private Resource expectedAddedFavoriteItemResource;

    private final String AUTH_TOKEN = "eyJraWQiOiJiYjRiNjM5MS04YWRhLTQzM2YtYjljMi00Mzg2ZTE3Y2JmZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb2NrQHJvY2sucnUiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2ODIzNjA2MzAsInNjb3BlIjpbIm9wZW5pZCJdLCJ1c2VyLWlkIjoiNmEyZTYzYTctYThiNy00YTVlLTk0MjItNmExNmVlOTYzZThkIiwiaXNzIjoiaHR0cDovLzUxLjI1MC4xMDIuMTI6OTAwMCIsImV4cCI6MTY4Mjk2NTQzMCwiaWF0IjoxNjgyMzYwNjMwLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXX0.pJcnmRliJY9g3n-TsAyRZxP0IiZNMkP8gMdiPItF9mcQZHuDOCzH8NWYstNUStVjZcRacxeL10IzsWvNWOfx2ZW7_hsJvqDrOJx30ykGahQY08xiaDelLg_KzVaAN-XqZL5-oUKybJfLC0mdcAejcfRch9APYwF-hwwU6KEvZK-UsfzoJik9sMvoCQCuK_2_unjHl9K3zUj-id-TE6bRLbTQI-E6hZSXQ_oU5K30EEUVxsf_Gc0GgMYBgRRzPIn7P9FqPgjDLkM9HH6HAQwWwBhJIziKQZSk-0S0JIS6JNawSqeLxNhgbgjLrEnpoiHiqvWwtPzQ8N6FOshGGQcSKA";

    private final String HEADER = "Bearer " + AUTH_TOKEN;

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void shouldCreateFavoritesReturnFavoritesExternalIdAndStatusCreated() throws Exception {
        try (MockedStatic<UUID> mockedStatic = Mockito.mockStatic(UUID.class)) {
            mockedStatic.when(UUID::randomUUID).thenReturn(FAVORITE_ID_UUID);
            mockedStatic.when(() -> UUID.fromString(PRODUCT_ID)).thenReturn(PRODUCT_ID_UUID);
            mockedStatic.when(() -> UUID.fromString(USER_ID)).thenReturn(USER_ID_UUID);

            MvcResult mvcResult = mockMvc.perform(
                            post(PUBLIC_API + "{userId}/favorites/{productId}", USER_ID, PRODUCT_ID)
                                    .header("Authorization", HEADER))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString();
            String expectedUUID = unwrapString(content);

            FavoriteItem favorite = repository.findById(1L)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

            assertAll(
                    () -> assertThat(expectedUUID).isEqualTo(FAVORITE_ID),

                    () -> assertThat(favorite)
                            .usingRecursiveComparison()
                            .ignoringFields("addedAt")
                            .isEqualTo(objectMapper.readValue(
                                    Files.readString(Path.of(expectedAddedFavoriteItemResource.getURI())), FavoriteItem.class))
            );

        }
    }

    @Test
    @Transactional
    @Sql("/db/insert-favorites-query.sql")
    void shouldGetFavoritesByUserIdReturnPageFavoritesAndStatusOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PUBLIC_API + "/{userId}/favorites", USER_ID).contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", HEADER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Page<FavoriteItemResponseDto> page = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<RestPageImpl<FavoriteItemResponseDto>>() {
                });

        UUID actualFavoritesId = page.getContent().get(0).externalId();
        UUID actualProductId = page.getContent().get(0).productId();
        UUID actualUserId = page.getContent().get(0).userId();

        assertAll(
                () -> assertNotNull(page),
                () -> assertEquals(1L, page.getTotalElements()),
                () -> assertEquals(1, page.getTotalPages()),
                () -> assertEquals(FAVORITE_ID_UUID, actualFavoritesId),
                () -> assertEquals(PRODUCT_ID_UUID, actualProductId),
                () -> assertEquals(USER_ID_UUID, actualUserId)
        );
    }

    @Test
    @Sql("/db/insert-favorites-query.sql")
    void shouldDeleteFavoritesReturnStatusNoContent() throws Exception {
        mockMvc.perform(
                        delete(PUBLIC_API + "/{userId}/favorites/{productId}", USER_ID, PRODUCT_ID)
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    void shouldDeleteFavoritesNegativeReturnStatusNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete(PUBLIC_API + "/{userId}/favorites/{productId}", USER_ID, PRODUCT_ID)
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        EntityNotFoundException entityNotFoundException =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EntityNotFoundException.class);
        assertNotNull(entityNotFoundException);
    }

    @Test
    void shouldGetFavoritesByUserIdNegativeReturnPageFavoritesAndStatusOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PUBLIC_API + "/{userId}/favorites", USER_ID, PageRequest.of(0, 10))
                                .header("Authorization", HEADER)
                                .contentType(MediaType.APPLICATION_JSON))
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