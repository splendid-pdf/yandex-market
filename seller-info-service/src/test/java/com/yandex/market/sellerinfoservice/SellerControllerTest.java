package com.yandex.market.sellerinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.sellerinfoservice.model.BusinessModel;
import com.yandex.market.sellerinfoservice.model.Seller;
import com.yandex.market.sellerinfoservice.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SqlGroup({
        @Sql(value = "classpath:db/insert_tests_fields.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:db/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SellerControllerTest {
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final SellerService sellerService;

    @Value("${spring.app.seller.url}")
    private String PATH_TO_SELLER;

    @Value("${spring.app.seller.json-path}" + "update/")
    private String RESOURCES_PATH_UPDATE;

/*    @Test
    void getSellerByExternalIdSellerFoundWithoutProblem() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c55");

        MvcResult mvcResult = mockMvc.perform(get(PATH_TO_SELLER + "{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        SellerResponseDto seller = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                SellerResponseDto.class);

        assertEquals(seller.externalId(), sellerId, "External IDs don't match");
    }*/

/*    @Test
    void getSellerByExternalIdSellerNotFound() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c99");

        MvcResult mvcResult = mockMvc.perform(get(PATH_TO_SELLER + "{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"statusCode\":\"NOT_FOUND\""),
                "The received status does not match the expected");
    }*/

/*    @Test
    void updateSellerSellerFoundAndFullyUpdated() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c51");

        Seller expectedSeller = Seller.builder()
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
                        PATH_TO_SELLER + "{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_full.json"))))
                .andExpect(status().isOk());

        SellerResponseDto seller = sellerService.getSellerByExternalId(sellerId);

        assertNotNull(seller, "The returned object is empty");

        AssertionsForClassTypes.assertThat(expectedSeller)
                .usingRecursiveComparison()
                .ignoringFields("id", "externalId")
                .isEqualTo(seller);
    }*/

/*    @Test
    void updateSellerSellerNotFound() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c98");

        MvcResult mvcResult = mockMvc.perform(put(
                        PATH_TO_SELLER + "{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_full.json"))))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"statusCode\":\"NOT_FOUND\""),
                "The received status does not match the expected");
    }*/

/*    @Test
    void updateSellerSellerFoundAndUpdatedOnlySellerName() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c52");

        String firstName = "Новое имя",
                lastName = "Новая фамилия";

        mockMvc.perform(put(
                        PATH_TO_SELLER + "{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_with_only_name.json"))))
                .andExpect(status().isOk())
                .andReturn();

        SellerResponseDto seller = sellerService.getSellerByExternalId(sellerId);

        assertAll("Values firstName and lastName are not as expected",
                () -> assertEquals(firstName, seller.firstName(), "FirstName values are not equal"),
                () -> assertEquals(lastName, seller.lastName(), "LastName values are not equal")
        );
    }*/

/*    @Test
    void updateSellerSellerFoundButWasTransferEmptyDto() throws Exception {
        UUID sellerId = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c53");

        SellerResponseDto sellerBefore = sellerService.getSellerByExternalId(sellerId);

        mockMvc.perform(put(
                        PATH_TO_SELLER + "{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(RESOURCES_PATH_UPDATE + "update_empty.json"))))
                .andExpect(status().isOk())
                .andReturn();

        SellerResponseDto sellerAfter = sellerService.getSellerByExternalId(sellerId);
        assertNotNull(sellerAfter, "The returned object is empty");

        AssertionsForClassTypes.assertThat(sellerBefore)
                .usingRecursiveComparison()
                .ignoringFields("id", "externalId")
                .isEqualTo(sellerAfter);
    }*/
}