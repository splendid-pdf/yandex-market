package com.yandex.market.shopservice.controllers.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.SpecialOfferDto;
import com.yandex.market.shopservice.dto.shop.SupportDto;
import com.yandex.market.shopservice.model.Location;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import com.yandex.market.shopservice.model.shop.Support;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

import static com.yandex.market.shopservice.model.shop.SpecialOfferType.PERSONAL_OFFER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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
        ResultActions actions = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());

        actions
                .andExpect(jsonPath("$.content[0].name").value("Eldorado"))
                .andExpect(jsonPath("$.content[0].token").value("DzdWIiOiIxMjM0NTY3ODkwIiwibmFtNTE2MjM5MDIyfQ"))
                .andExpect(jsonPath("$.content[0].logoUrl").value("https://static.eldorado.ru/espa/l.42.0-ab-esp-5648.3-Ps0BSwVDAKQkPjSPyBVlu/static_spa/assets/logo.dc65dadd.svg"))
                .andExpect(jsonPath("$.content[0].rating").value(4.8));

        actions
                .andExpect(jsonPath("$.content[0].support.number").value("8-800-250-25-25"))
                .andExpect(jsonPath("$.content[0].support.email").value("eldorado@sup.com"));

        actions
                .andExpect(jsonPath("$.content[0].legalEntityAddress.country").value("Russia"))
                .andExpect(jsonPath("$.content[0].legalEntityAddress.region").value("Moscow Region"))
                .andExpect(jsonPath("$.content[0].legalEntityAddress.city").value("Moscow"))
                .andExpect(jsonPath("$.content[0].legalEntityAddress.street").value("Krizhanovskaya"))
                .andExpect(jsonPath("$.content[0].legalEntityAddress.houseNumber").value("5B"))
                .andExpect(jsonPath("$.content[0].legalEntityAddress.officeNumber").value("12C"))
                .andExpect(jsonPath("$.content[0].legalEntityAddress.postcode").value("117218"));

        actions
                .andExpect(jsonPath("$.content[0].specialOffers[0].shopSystem").value("87ba7a03-054e-4c7f-ac35-f4802d66cec3"))
                .andExpect(jsonPath("$.content[0].specialOffers[0].name").value("Подарок на день рождение"))
                .andExpect(jsonPath("$.content[0].specialOffers[0].type").value("DISCOUNT"))
                .andExpect(jsonPath("$.content[0].specialOffers[0].value").value("10"))
                .andExpect(jsonPath("$.content[0].specialOffers[0].terms").value("Скидка предоставляется в течение 7 дней до дня рождения и 7 дней после"));

    }

    @Test
    void createNewShopSystemWithoutBranches_returnShopSystemExternalIdAnd201Created() throws Exception {
        ShopSystemRequestDto shopSystemRequestDto = getShopSystemRequestDto();

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(shopSystemRequestDto))
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
        ShopSystemRequestDto shopSystemRequestDto = getShopSystemRequestDto();
        BranchDto branchDto = getBranchDto();

        shopSystemRequestDto.setBranches(Set.of(branchDto));

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(shopSystemRequestDto))
        );

        ShopSystem newShopSystem = shopSystemRepository.findById(2L).orElseThrow(EntityNotFoundException::new);

        result
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newShopSystem.getExternalId().toString())));

        assertThat(newShopSystem.getBranches()).hasSize(1);
    }

    @Test
    void getShopSystemByExternalId_returnShopSystemAnd200Ok() throws Exception {
        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        ResultActions actions = mockMvc.perform(get(url + "/{externalId}", shopSystem.getExternalId()))
                .andExpect(status().isOk());

        actions
                .andExpect(jsonPath("$.name").value("Eldorado"))
                .andExpect(jsonPath("$.token").value("DzdWIiOiIxMjM0NTY3ODkwIiwibmFtNTE2MjM5MDIyfQ"))
                .andExpect(jsonPath("$.logoUrl").value("https://static.eldorado.ru/espa/l.42.0-ab-esp-5648.3-Ps0BSwVDAKQkPjSPyBVlu/static_spa/assets/logo.dc65dadd.svg"))
                .andExpect(jsonPath("$.rating").value(4.8));

        actions
                .andExpect(jsonPath("$.support.number").value("8-800-250-25-25"))
                .andExpect(jsonPath("$.support.email").value("eldorado@sup.com"));

        actions
                .andExpect(jsonPath("$.legalEntityAddress.country").value("Russia"))
                .andExpect(jsonPath("$.legalEntityAddress.region").value("Moscow Region"))
                .andExpect(jsonPath("$.legalEntityAddress.city").value("Moscow"))
                .andExpect(jsonPath("$.legalEntityAddress.street").value("Krizhanovskaya"))
                .andExpect(jsonPath("$.legalEntityAddress.houseNumber").value("5B"))
                .andExpect(jsonPath("$.legalEntityAddress.officeNumber").value("12C"))
                .andExpect(jsonPath("$.legalEntityAddress.postcode").value("117218"));

        actions
                .andExpect(jsonPath("$.specialOffers[0].shopSystem").value("87ba7a03-054e-4c7f-ac35-f4802d66cec3"))
                .andExpect(jsonPath("$.specialOffers[0].name").value("Подарок на день рождение"))
                .andExpect(jsonPath("$.specialOffers[0].type").value("DISCOUNT"))
                .andExpect(jsonPath("$.specialOffers[0].value").value("10"))
                .andExpect(jsonPath("$.specialOffers[0].terms").value("Скидка предоставляется в течение 7 дней до дня рождения и 7 дней после"));

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
    @Transactional
    @Rollback(value = false)
    void updateSystemShopByExternalId_return200OkAndUpdateFields() throws Exception {
        ShopSystemRequestDto shopSystemRequest = getShopSystemRequestDto();

        shopSystemRequest.setName("Splendid");
        shopSystemRequest.setToken("DooMIiOiIxMjQWERT3ODkwIivimmFtNTE2MjM5ZXCnpR");
        shopSystemRequest.setLogoUrl("https://static.eldorado.ru/espa/short-logo.dc65dadd.svg");

        shopSystemRequest.setSupport(new SupportDto("8-800-250-34-34", "Splendid@support.com"));

        LocationDto locationDto = getLocationDto();
        shopSystemRequest.setLegalEntityAddress(locationDto);

        Set<@Valid SpecialOfferDto> specialOffersDto = shopSystemRequest.getSpecialOffers();
        SpecialOfferDto specialOfferDto = getSpecialOfferDto();
        specialOffersDto.remove(specialOfferDto);
        specialOffersDto.add(specialOfferDto);

        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(put(url + "/{externalId}", shopSystem.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shopSystemRequest))
                )
                .andExpect(status().isOk());

        assertThat(shopSystem.getName()).isEqualTo("Splendid");
        assertThat(shopSystem.getToken()).isEqualTo("DooMIiOiIxMjQWERT3ODkwIivimmFtNTE2MjM5ZXCnpR");
        assertThat(shopSystem.getLogoUrl()).isEqualTo("https://static.eldorado.ru/espa/short-logo.dc65dadd.svg");

        Support support = shopSystem.getSupport();

        assertThat(support.getNumber()).isEqualTo("8-800-250-34-34");
        assertThat(support.getEmail()).isEqualTo("Splendid@support.com");

        Location location = shopSystem.getLegalEntityAddress();

        assertThat(location.getCountry()).isEqualTo("Russia2");
        assertThat(location.getRegion()).isEqualTo("Kirovskiy region");
        assertThat(location.getCity()).isEqualTo("Kirov");
        assertThat(location.getStreet()).isEqualTo("Voljanskaya");
        assertThat(location.getHouseNumber()).isEqualTo("2P");
        assertThat(location.getOfficeNumber()).isEqualTo("9M");
        assertThat(location.getPostcode()).isEqualTo("337129");

        Set<SpecialOffer> specialOffers = shopSystem.getSpecialOffers();

        assertThat(specialOffers).extracting("name", "type", "value", "terms")
                .contains(tuple("Подарок на день рождение", PERSONAL_OFFER, 15, "Персональное предложение на день рождение"));

    }

    private static LocationDto getLocationDto() {
        return LocationDto.builder()
                .country("Russia2")
                .region("Kirovskiy region")
                .city("Kirov")
                .street("Voljanskaya")
                .houseNumber("2P")
                .officeNumber("9M")
                .postcode("337129")
                .build();
    }

    private static SpecialOfferDto getSpecialOfferDto() {
        return SpecialOfferDto.builder()
                .name("Подарок на день рождение").type(PERSONAL_OFFER)
                .value(15).terms("Персональное предложение на день рождение").build();
    }

    private BranchDto getBranchDto() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/CreateBranchRequest.json"));
        return mapper.readValue(json, BranchDto.class);
    }

    private ShopSystemRequestDto getShopSystemRequestDto() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/CreateShopsystemRequest.json"));
        return mapper.readValue(json, ShopSystemRequestDto.class);
    }
}
