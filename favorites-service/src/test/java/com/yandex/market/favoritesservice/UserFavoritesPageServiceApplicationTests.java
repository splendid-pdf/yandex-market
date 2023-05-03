package com.yandex.market.favoritesservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.yandex.market.auth.model.Role;
import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.util.RestPageImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@Disabled
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SqlGroup({
        @Sql(
                scripts = "classpath:db/truncate_favorites.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
class UserFavoritesPageServiceApplicationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final UserFavoritesPageRepository repository;

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

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void shouldCreateFavoritesReturnFavoritesExternalIdAndStatusCreated() throws Exception {
        try (MockedStatic<UUID> mockedStatic = Mockito.mockStatic(UUID.class)) {
            mockedStatic.when(UUID::randomUUID).thenReturn(FAVORITE_ID_UUID);
            mockedStatic.when(() -> UUID.fromString(PRODUCT_ID)).thenReturn(PRODUCT_ID_UUID);
            mockedStatic.when(() -> UUID.fromString(USER_ID)).thenReturn(USER_ID_UUID);

            MvcResult mvcResult = mockMvc.perform(
                            post(PUBLIC_API + "{userId}/favorites", USER_ID)
                                    .with(authentication(getToken())))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString();
            String expectedUUID = unwrapString(content);

            UserFavoritesPage favorite = repository.findById(1L)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

            assertAll(
                    () -> assertThat(expectedUUID).isEqualTo(FAVORITE_ID),

                    () -> assertThat(favorite)
                            .usingRecursiveComparison()
                            .ignoringFields("addedAt")
                            .isEqualTo(objectMapper.readValue(
                                    Files.readString(Path.of(expectedAddedFavoriteItemResource.getURI())), UserFavoritesPage.class))
            );

        }
    }

    @Test
    @Transactional
    @Sql("/db/insert-favorites-query.sql")
    void shouldGetFavoritesByUserIdReturnPageFavoritesAndStatusOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(PUBLIC_API + "/{userId}/favorites", USER_ID).contentType(MediaType.APPLICATION_JSON)
                                .with(authentication(getToken())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Page<FavoriteProductRequest> page = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<RestPageImpl<FavoriteProductRequest>>() {
                });

        UUID actualFavoritesId = page.getContent().get(0).productId();

        assertAll(
                () -> assertNotNull(page),
                () -> assertEquals(1L, page.getTotalElements()),
                () -> assertEquals(1, page.getTotalPages()),
                () -> assertEquals(FAVORITE_ID_UUID, actualFavoritesId)
        );
    }

    @Test
    @Sql("/db/insert-favorites-query.sql")
    void shouldDeleteFavoritesReturnStatusNoContent() throws Exception {
        mockMvc.perform(
                        delete(PUBLIC_API + "/{userId}/favorites/{productId}", USER_ID, PRODUCT_ID)
                                .with(authentication(getToken()))
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
                                .with(authentication(getToken()))
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
                                .with(authentication(getToken()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThrows(ValueInstantiationException.class, () -> objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        new TypeReference<RestPageImpl<FavoriteProductRequest>>() {
                        }),
                "Page size must not be less than one"
        );
    }

    private String unwrapString(String str) {
        return str.substring(1, str.length() - 1);
    }

    private JwtAuthenticationToken getToken() {

        Jwt jwt = Jwt.withTokenValue("token")
                .header("kid", "5ab08392-4f53-49ee-8353-1f6be208840b")
                .header("alg", "RS256")
                .claim("sub", "rock@rock.ru")
                .claim("user-id", "6a2e63a7-a8b7-4a5e-9422-6a16ee963e8d")
                .claim("authorities", List.of(Role.USER.getKey()))
                .build();

        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.USER.getKey());
        return new JwtAuthenticationToken(jwt, authorities);
    }
}