package com.yandex.market.shopservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.shopservice.controllers.shop.ShopSystemController;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponsesDto;
import com.yandex.market.shopservice.service.shop.ShopSystemService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShopSystemController.class)
class ShopSystemControllerTest {

    @Value("${spring.application.url.shop}")
    private String url;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopSystemService shopSystemService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAllShopSystems_returnDefaultPagingPageAnd200OkWhenPagingQueryParamsNotDefined() throws Exception {
        PageRequest defaultPageable = PageRequest.of(0, 20);
        PageImpl<ShopSystemResponsesDto> page = new PageImpl<>(Collections.emptyList(), defaultPageable, 43);

        when(shopSystemService.getAllShopSystems(defaultPageable)).thenReturn(page);

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(page.getTotalElements()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllShopSystems_returnPagingPageAnd200OkWhenQueryParamsDefined() throws Exception {
        PageRequest pageable = PageRequest.of(2, 10);
        PageImpl<ShopSystemResponsesDto> page = new PageImpl<>(Collections.emptyList(), pageable, 43);

        when(shopSystemService.getAllShopSystems(pageable)).thenReturn(page);

        mockMvc.perform(get(url + "?page=2&size=10"))
                .andDo(print())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.number").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.totalElements").value(page.getTotalElements()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllShopSystems_returnDefaultPagingPageAnd200OkWhenPagingQueryParamsWrong() throws Exception {
        PageRequest defaultPageable = PageRequest.of(0, 20);
        PageImpl<ShopSystemResponsesDto> page = new PageImpl<>(Collections.emptyList(), defaultPageable, 43);

        when(shopSystemService.getAllShopSystems(defaultPageable)).thenReturn(page);

        mockMvc.perform(get(url + "?page=-2&size=-10"))
                .andDo(print())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(page.getTotalElements()))
                .andExpect(status().isOk());
    }

    @Test
    void createNewShopSystem_return201Created() throws Exception {
        ShopSystemRequestDto request = new ShopSystemRequestDto();
        request.setName("splendid");

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void createNewShopSystem_throwValidationExceptionNameNotBlankAnd400BadRequestWhenNameIsBlank() throws Exception {
        ShopSystemRequestDto request = new ShopSystemRequestDto();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getShopSystemByExternalId_returnShopSystemAnd200Ok() throws Exception {
        UUID externalId = UUID.randomUUID();
        ShopSystemResponsesDto response = new ShopSystemResponsesDto();

        when(shopSystemService.getShopSystemDtoByExternalId(externalId)).thenReturn(response);

        mockMvc.perform(get(url + "/{externalId}", externalId))
                .andDo(print())
                .andExpect(jsonPath("$").exists())
                .andExpect(status().isOk());
    }

    @Test
    void getShopSystemByExternalId_throwEntityNotFoundExceptionAnd404NotFound() throws Exception {
        UUID externalId = UUID.randomUUID();

        when(shopSystemService.getShopSystemDtoByExternalId(externalId))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(url + "/{externalId}", externalId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShopSystemByExternalId_return200Ok() throws Exception {
        UUID externalId = UUID.randomUUID();

        mockMvc.perform(delete(url + "/{externalId}", externalId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShopSystemByExternalId_throwEntityNotFoundExceptionAnd404NotFound() throws Exception {
        UUID externalId = UUID.randomUUID();

        doThrow(EntityNotFoundException.class)
                .when(shopSystemService)
                .deleteShopSystemByExternalId(externalId);

        mockMvc.perform(delete(url + "/{externalId}", externalId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSystemShopByExternalId_return200Ok() throws Exception {
        UUID externalId = UUID.randomUUID();
        ShopSystemRequestDto request = new ShopSystemRequestDto();
        request.setName("splendid");

        mockMvc.perform(put(url + "/{externalId}", externalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    void updateSystemShopByExternalId_throwEntityNotFoundExceptionAnd404NotFound() throws Exception {
        UUID externalId = UUID.randomUUID();
        ShopSystemRequestDto request = new ShopSystemRequestDto();
        request.setName("splendid");

        doThrow(EntityNotFoundException.class)
                .when(shopSystemService)
                .updateShopSystemByExternalId(externalId, request);

        mockMvc.perform(put(url + "/{externalId}", externalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSystemShopByExternalId_throwValidationExceptionNameNotBlankAnd400BadRequestWhenNameIsBlank() throws Exception {
        UUID externalId = UUID.randomUUID();
        ShopSystemRequestDto request = new ShopSystemRequestDto();

        mockMvc.perform(put(url + "/{externalId}", externalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
}
