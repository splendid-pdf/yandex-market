package com.yandex.market.shopservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.SupportDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:insert_shopsystem.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShopSystemControllerTest {

    @Value("${spring.application.url.shop}")
    private String url;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopSystemRepository shopSystemRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getAllShopSystems_returnPageWithShopSystemsAnd200Ok() throws Exception {
        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(content().string(containsString(shopSystem.getName())))
                .andExpect(content().string(containsString(shopSystem.getToken())))
                .andExpect(content().string(containsString(shopSystem.getSupport().getNumber())))
                .andExpect(content().string(containsString(shopSystem.getSupport().getEmail())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getCountry())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getCity())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getStreet())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getHouseNumber())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getOfficeNumber())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getPostcode())))
                .andExpect(content().string(containsString(shopSystem.getLogoUrl())))
                .andExpect(content().string(containsString(String.valueOf(shopSystem.getRating()))));
    }

    @Test
    void createNewShopSystemWithoutBranches_returnShopSystemExternalIdAnd201Created() throws Exception {
        String request = getShopSystemRequestJson();

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        );

        ShopSystem newShopSystem = shopSystemRepository.findById(2L).orElseThrow(EntityNotFoundException::new);

        result
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newShopSystem.getExternalId().toString())));
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void createNewShopSystemWithBranches_returnShopSystemExternalIdAnd201Created() throws Exception {
        ShopSystemRequestDto shopSystemRequestDto = mapper.readValue(getShopSystemRequestJson(), ShopSystemRequestDto.class);
        BranchDto branchDto = mapper.readValue(getBranchRequestJson(), BranchDto.class);

        shopSystemRequestDto.setBranches(Set.of(branchDto));

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(shopSystemRequestDto))
        );

        ShopSystem newShopSystem = shopSystemRepository.findById(2L).orElseThrow(EntityNotFoundException::new);

        result
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newShopSystem.getExternalId().toString())));

        assertThat(newShopSystem.getBranches().size()).isEqualTo(1);
    }

    @Test
    void getShopSystemByExternalId_returnShopSystemAnd200Ok() throws Exception {
        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(get(url + "/{externalId}", shopSystem.getExternalId()))
                .andExpect(content().string(containsString(shopSystem.getName())))
                .andExpect(content().string(containsString(shopSystem.getToken())))
                .andExpect(content().string(containsString(shopSystem.getSupport().getNumber())))
                .andExpect(content().string(containsString(shopSystem.getSupport().getEmail())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getCountry())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getCity())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getStreet())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getHouseNumber())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getOfficeNumber())))
                .andExpect(content().string(containsString(shopSystem.getLegalEntityAddress().getPostcode())))
                .andExpect(content().string(containsString(shopSystem.getLogoUrl())))
                .andExpect(content().string(containsString(String.valueOf(shopSystem.getRating()))));
    }

    @Test
    void deleteShopSystemByExternalId_return200OkAndDisableShopSystem() throws Exception {
        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(delete(url + "/{externalId}", shopSystem.getExternalId()))
                .andExpect(status().isOk());

        ShopSystem updatedShopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        assertThat(updatedShopSystem.isDisabled()).isTrue();
    }

    @Test
    void updateSystemShopByExternalId_return200OkAndUpdateFields() throws Exception {
        String request = getShopSystemRequestJson();
        ShopSystemRequestDto shopSystemRequest = mapper.readValue(request, ShopSystemRequestDto.class);

        shopSystemRequest.setName("Splendid");
        shopSystemRequest.setSupport(new SupportDto("8-800-250-34-34", "Splendid@support.com"));

        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        mockMvc.perform(put(url + "/{externalId}", shopSystem.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shopSystemRequest))
                )
                .andExpect(status().isOk());

        ShopSystem updatedShopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        assertThat(updatedShopSystem.getName()).isEqualTo("Splendid");
        assertThat(updatedShopSystem.getSupport().getNumber()).isEqualTo("8-800-250-34-34");
        assertThat(updatedShopSystem.getSupport().getEmail()).isEqualTo("Splendid@support.com");
    }

    private static String getBranchRequestJson() throws IOException {
        return Files.readString(Path.of("src/test/resources/CreateBranchRequest.json"));
    }

    private static String getShopSystemRequestJson() throws IOException {
        return Files.readString(Path.of("src/test/resources/CreateShopsystemRequest.json"));
    }
}
