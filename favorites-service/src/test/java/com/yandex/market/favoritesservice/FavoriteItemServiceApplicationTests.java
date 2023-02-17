package com.yandex.market.favoritesservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.exception.NotFoundException;
import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import com.yandex.market.favoritesservice.repository.FavoriteItemRepository;
import com.yandex.market.util.RestPageImpl;
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

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FavoriteItemServiceApplicationTests {
    private final static String USER_ID = "9fb57cbe-a232-4b33-beb0-0e0d8a9d9e20";
    private final static String PRODUCT_ID = "5d61ef1a-b76a-4675-b791-356ab3b834d5";
    private final static String FAVORITE_ID = "e5c6b86d-d167-4f40-a5d0-d1f7e66c5776";
    
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
        UUID favoriteId = UUID.fromString(FAVORITE_ID);
        UUID projectId = UUID.fromString(PRODUCT_ID);
        UUID userId = UUID.fromString(USER_ID);
        try(MockedStatic<UUID> mockedStatic = Mockito.mockStatic(UUID.class)) {
            mockedStatic.when(UUID::randomUUID).thenReturn(favoriteId);
            mockedStatic.when(() -> UUID.fromString(PRODUCT_ID)).thenReturn(projectId);
            mockedStatic.when(() -> UUID.fromString(USER_ID)).thenReturn(userId);

            MvcResult mvcResult = mockMvc.perform(post(PUBLIC_API + "{userId}/favorites/{productId}", USER_ID, PRODUCT_ID))
                    .andExpect(status().isCreated())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString();
            String expectedUUID = unwrapString(content);
            FavoriteItem favorite = repository.findById(1L)
                    .orElseThrow(() -> new NotFoundException("Entity not found"));

            assertThat(expectedUUID).isEqualTo(FAVORITE_ID);
            assertThat(favorite)
                    .usingRecursiveComparison()
                    .ignoringFields("addedAt")
                    .isEqualTo(objectMapper.readValue(Files.readString(Path.of(expectedAddedFavoriteItemResource.getURI())), FavoriteItem.class));
        }
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void getFavoritesByUserId_returnPageFavoritesAnd200_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PUBLIC_API + "/{userId}/favorites", FAVORITE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Page<FavoriteItemResponseDto> page =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<RestPageImpl<FavoriteItemResponseDto>>() {
                        });

        UUID actualProductId = page.getContent().get(0).productId();

        assertNotNull(page);
        assertEquals(page.getTotalElements(), 1L);
        assertEquals(page.getTotalPages(), 1L);
        assertEquals(PRODUCT_ID, actualProductId);
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void deleteFavorites_return204NoContent() throws Exception {
        mockMvc.perform(delete(PUBLIC_API + "/{userId}/favorites/{productId}", FAVORITE_ID, PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
        assertNull(repository.findById(1L).orElse(null));
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void deleteFavoritesNegative_return404NotFound() throws Exception {
        mockMvc.perform(delete(PUBLIC_API + "/{userId}/favorites/{productId}", FAVORITE_ID, PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @Sql("/db/insertFavoritesQuery.sql")
    public void getFavoritesByUserIdNegative_returnPageFavoritesAnd200_Ok() throws Exception {
        mockMvc.perform(get(PUBLIC_API + "/{userId}/favorites", FAVORITE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    private String unwrapString(String str) {
        return str.substring(1, str.length() - 1);
    }
}