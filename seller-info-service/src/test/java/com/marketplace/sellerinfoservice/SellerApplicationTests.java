package com.marketplace.sellerinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.sellerinfoservice.dto.SellerRegistration;
import com.marketplace.sellerinfoservice.dto.SellerResponseDto;
import com.marketplace.sellerinfoservice.model.BusinessModel;
import com.marketplace.sellerinfoservice.service.SellerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testcontainers")
class SellerApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SellerService sellerService;
    private static final String SELLER_RESPONSE_JSON = "src/test/resources/json/create/seller_response.json";
    private static final String CREATE_SELLER_REQUEST_DTO_JSON = "src/test/resources/json/create/create_seller_request_dto.json";
    private static final String CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON = "src/test/resources/json/create/create_seller_request_dto_negative.json";
    private final UUID SELLER_ID = UUID.fromString("44329c76-db69-425d-a5ef-f71cefec44db");
    private final UUID SELLER_ID_NEGATIVE = UUID.fromString("77678201-f3c8-4d5c-a628-2344eef50c55");
    @Value("${spring.app.seller.url}")
    private String PATH_TO_SELLER;
    @Value("${spring.app.seller.json-path}" + "update/")
    private String RESOURCES_PATH_UPDATE;

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное получение продавца по id")
    void getSellerByExternalIdSellerFoundWithoutProblem() throws Exception {

        mockMvc
                .perform(get(PATH_TO_SELLER + "/{sellerId}", SELLER_ID)
                        .with(authentication(getToken())))
                .andExpect(content().json(Files.readString(Path.of(SELLER_RESPONSE_JSON))))
                .andReturn();
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Попытка получения продавца по id, которого не существует в базе данных")
    void getSellerByExternalIdSellerNotFound() throws Exception {

        UUID sellerId = UUID.fromString("37678209-f3c8-4d5c-a628-2344eef50c99");

        mockMvc.perform(get(PATH_TO_SELLER + "/{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken())))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное обновление всех полей продавца")
    void updateSellerFoundAndFullyUpdated() throws Exception {

        SellerResponseDto expectedSeller = SellerResponseDto.builder()
                .firstName("Новое имя")
                .lastName("Новая фамилия")
                .email("newemail@mail.ru")
                .legalAddress("Ул Новая, 1")
                .companyName("Новая компания")
                .imageUrl("new.png")
                .businessModel(BusinessModel.IP)
                .ITN("123456789")
                .PSRN("123456")
                .BIC("1234")
                .paymentAccount("ACCOUNT123456")
                .corporateAccount("SACCOUNT123")
                .build();

        mockMvc.perform(put(
                        PATH_TO_SELLER + "/{sellerId}", SELLER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_full.json"))))
                .andExpect(status().isOk());

        SellerResponseDto seller = sellerService.getSellerByExternalId(SELLER_ID);

        assertNotNull(seller, "The returned object is empty");

        AssertionsForClassTypes.assertThat(expectedSeller)
                .usingRecursiveComparison()
                .ignoringFields("id", "externalId")
                .isEqualTo(seller);
    }


    @Test
    @DisplayName("Попытка обноваления продавца, которого не существует в ббазе данных")
    void updateSellerNotFound() throws Exception {

        mockMvc.perform(put(
                        PATH_TO_SELLER + "/{sellerId}", SELLER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_full.json"))))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное обновление части полей продавца")
    void updateSellerFoundAndUpdatedOnlySellerName() throws Exception {

        String firstName = "Новое имя",
                lastName = "Новая фамилия";

        mockMvc.perform(put(
                        PATH_TO_SELLER + "/{sellerId}", SELLER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_with_only_name.json"))))
                .andExpect(status().isOk())
                .andReturn();

        SellerResponseDto seller = sellerService.getSellerByExternalId(SELLER_ID);

        assertAll("Values firstName and lastName are not as expected",
                () -> assertEquals(firstName, seller.firstName(), "FirstName values are not equal"),
                () -> assertEquals(lastName, seller.lastName(), "LastName values are not equal")
        );
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Попытка обновления продавца с пустым телом запроса")
    void updateSellerFoundButWasTransferEmptyDto() throws Exception {

        SellerResponseDto sellerBefore = sellerService.getSellerByExternalId(SELLER_ID);

        mockMvc.perform(put(
                        PATH_TO_SELLER + "/{sellerId}", SELLER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_empty.json"))))
                .andExpect(status().isOk())
                .andReturn();

        SellerResponseDto sellerAfter = sellerService.getSellerByExternalId(SELLER_ID);
        assertNotNull(sellerAfter, "The returned object is empty");

        AssertionsForClassTypes.assertThat(sellerBefore)
                .usingRecursiveComparison()
                .ignoringFields("id", "externalId")
                .isEqualTo(sellerAfter);
    }

    @Test
    @DisplayName("Успешное создание продавца")
    void createSeller() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post(PATH_TO_SELLER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON))))
                .andExpect(status().isCreated())
                .andReturn();

        String expectedEmail = "arkady@gmail.ru";
        UUID actualSellerExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);
        String email = sellerService.getSellerByExternalId(actualSellerExternalId).email();

        Assertions.assertNotNull(sellerService.getSellerByExternalId(actualSellerExternalId));
        Assertions.assertEquals(expectedEmail, email);
    }

    @Test
    @DisplayName("Попытка создания продавца с невалидными телом запроса")
    void createSellerNegative() throws Exception {

        mockMvc.perform(post(PATH_TO_SELLER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Попытка создания продавца, который уже существует в базе данных")
    void createSellerNegativeControllerExistException() throws Exception {

        mockMvc.perform(post(PATH_TO_SELLER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(getToken()))
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON))))
                .andExpect(status().isConflict());

        Assertions.assertThrows(EntityExistsException.class, () -> sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRegistration.class)));
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное удаление продавца из базы данных")
    void deleteSellerController() throws Exception {

        mockMvc.perform(delete(PATH_TO_SELLER + "/{sellerId}", SELLER_ID)
                        .with(authentication(getToken())))
                .andExpect(status().isOk());

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .deleteSellerByExternalId(SELLER_ID));
    }

    @Test
    @DisplayName("Попытка удаления продавца, которого не существует в баазе данных")
    void deleteSellerControllerNegative() throws Exception {

        mockMvc.perform(delete(PATH_TO_SELLER + SELLER_ID_NEGATIVE))
                .andExpect(status().isUnauthorized());
    }

    private JwtAuthenticationToken getToken() {

        Jwt jwt = Jwt.withTokenValue("token")
                .header("kid", "5ab08392-4f53-49ee-8353-1f6be208840b")
                .header("alg", "RS256")
                .claim("sub", "arkady@gmail.ru")
                .claim("seller-id", "44329c76-db69-425d-a5ef-f71cefec44db")
                .claim("authorities", List.of("ROLE_SELLER"))
                .build();

        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_SELLER");
        return new JwtAuthenticationToken(jwt, authorities);
    }
}