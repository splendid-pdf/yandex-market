package com.marketplace.sellerinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.sellerinfoservice.dto.SellerRegistration;
import com.marketplace.sellerinfoservice.dto.SellerRequestDto;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testContainers")
class SellerUnitTest {

    private final ObjectMapper objectMapper;
    private final SellerService sellerService;
    private final UUID SELLER_ID = UUID.fromString("44329c76-db69-425d-a5ef-f71cefec44db");
    private static final String SELLER_REQUEST_JSON = "src/test/resources/json/create/create_seller_request_dto.json";

    @Test
    @Transactional
    @DisplayName("Успешное создание нового продавца")
    void createSellerService() throws IOException {

        UUID actualSellerExternalId = sellerService
                .createSeller(objectMapper
                        .readValue(Files.readString(Path.of(SELLER_REQUEST_JSON)), SellerRegistration.class));
        Assertions.assertNotNull(sellerService.getSellerByExternalId(actualSellerExternalId));
    }

    @Test
    @Transactional
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Попытка создания продавца, который уже существует в базе данных")
    void createSellerServiceNegativeServiceExistException() {

        Assertions.assertThrows(EntityExistsException.class, () -> sellerService
                .createSeller(objectMapper
                        .readValue(Files.readString(Path.of(SELLER_REQUEST_JSON)), SellerRegistration.class)));
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное послучение продавца по id")
    void getSellerById() {

        SellerResponseDto expectedSeller = SellerResponseDto.builder()
                .firstName("aleks")
                .lastName("zhi")
                .email("arkady@gmail.ru")
                .legalAddress("testcity")
                .companyName("companyName")
                .imageUrl("url")
                .businessModel(BusinessModel.IP)
                .ITN("4321")
                .PSRN("9876")
                .BIC("1234")
                .paymentAccount("ACCOUNT123456")
                .corporateAccount("corporateAccount")
                .build();

        AssertionsForClassTypes.assertThat(expectedSeller)
                .usingRecursiveComparison()
                .ignoringFields("id", "externalId")
                .isEqualTo(sellerService.getSellerByExternalId(SELLER_ID));
    }

    @Test
    @DisplayName("Попытка получения продавца по id, которого не существует в базе данных")
    void getSellerNotFound() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService.getSellerByExternalId(SELLER_ID));
    }

    @Test
    @Transactional
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное обновление продавца")
    void updateSeller() {

        SellerRequestDto expectedSeller = SellerRequestDto.builder()
                .firstName("aleksTest")
                .lastName("zhiTest")
                .email("test@gmail.ru")
                .legalAddress("testcity")
                .companyName("companyName")
                .imageUrl("url")
                .businessModel(BusinessModel.IP)
                .ITN("43211")
                .PSRN("98762")
                .BIC("12345")
                .paymentAccount("ACCOUNT12345")
                .corporateAccount("corporateAccountTest")
                .build();

        AssertionsForClassTypes.assertThat(expectedSeller)
                .usingRecursiveComparison()
                .ignoringFields("id", "externalId")
                .isEqualTo(sellerService.updateSeller(SELLER_ID, expectedSeller));
    }

    @Test
    @DisplayName("Попытка обновление продавца, которого не существует в базе данных")
    void updateSellerNotFound() {

        SellerRequestDto expectedSeller = SellerRequestDto.builder()
                .firstName("aleksTest")
                .lastName("zhiTest")
                .email("test@gmail.ru")
                .legalAddress("testcity")
                .companyName("companyName")
                .imageUrl("url")
                .businessModel(BusinessModel.IP)
                .ITN("43211")
                .PSRN("98762")
                .BIC("12345")
                .paymentAccount("ACCOUNT12345")
                .corporateAccount("corporateAccountTest")
                .build();

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService.updateSeller(SELLER_ID, expectedSeller));
    }

    @Test
    @Sql("/db/insert_test_seller.sql")
    @DisplayName("Успешное удаление продавца")
    void deleteSellerService() {

        sellerService.deleteSellerByExternalId(SELLER_ID);

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .getSellerByExternalId(SELLER_ID));
    }

    @Test
    @DisplayName("Попытка удаления продавца, которого не существует в базе данных")
    void deleteSellerServiceNegative() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .deleteSellerByExternalId(SELLER_ID));
    }
}