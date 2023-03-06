package com.yandex.market.sellerinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.service.SellerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testcontainers")
class SellerInfoServiceApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SellerService sellerService;
    public static final String CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON = "src/test/resources/json/create/CreateSellerRequestDtoNegative.json";
    public static final String CREATE_SELLER_REQUEST_DTO_JSON = "src/test/resources/json/create/CreateSellerRequestDto.json";
    private final UUID SELLER_EXTERNAL_ID = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c54");
    private final UUID SELLER_EXTERNAL_ID_NEGATIVE = UUID.fromString("37678201-f3c8-4d5c-a628-2344eef50c55");

    @Test
    @Transactional
    public void createSellerController() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/public/api/v1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON))))
                .andExpect(status().isCreated())
                .andReturn();

        String expectedFirstName = "Alex";
        UUID actualSellerExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);
        String actualFirstName = sellerService.getSellerByExternalId(actualSellerExternalId).getFirstName();

        Assertions.assertNotNull(sellerService.getSellerByExternalId(actualSellerExternalId));
        Assertions.assertEquals(expectedFirstName, actualFirstName);
    }

    @Test
    @Transactional
    public void createSellerNegativeController() throws Exception {
        mockMvc.perform(post("/public/api/v1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestSeller.sql")
    public void createSellerNegativeControllerExistException() throws Exception {
        mockMvc.perform(post("/public/api/v1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON))))
                .andExpect(status().isConflict());

        Assertions.assertThrows(EntityExistsException.class, () -> sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRequestDto.class)));
    }

    @Test
    @Transactional
    public void createSellerService() throws IOException {
        UUID actualSellerExternalId = sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRequestDto.class));
        Assertions.assertNotNull(sellerService.getSellerByExternalId(actualSellerExternalId));
    }

    @Test
    @Transactional
    public void createSellerServiceNegative() {
        Assertions.assertThrows(InvalidFormatException.class, () -> sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON)), SellerRequestDto.class)));
    }

    @Test
    @Transactional
    @Sql("/db/insertTestSeller.sql")
    public void createSellerServiceNegativeServiceExistException() {
        Assertions.assertThrows(EntityExistsException.class, () -> sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRequestDto.class)));
    }

    @Test
    @Transactional
    @Sql("/db/insertTestSeller.sql")
    public void deleteSellerController() throws Exception {
        mockMvc.perform(delete("/public/api/v1/sellers/" + SELLER_EXTERNAL_ID))
                .andExpect(status().isOk());

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .getSellerByExternalId(SELLER_EXTERNAL_ID));
    }

    @Test
    @Transactional
    @Sql("/db/insertTestSeller.sql")
    public void deleteSellerControllerNegative() throws Exception {
        mockMvc.perform(delete("/public/api/v1/sellers/" + SELLER_EXTERNAL_ID_NEGATIVE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestSeller.sql")
    public void deleteSellerService() {
        sellerService.deleteSeller(SELLER_EXTERNAL_ID);

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .getSellerByExternalId(SELLER_EXTERNAL_ID));
    }

    @Test
    @Transactional
    public void deleteSellerServiceNegative() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .deleteSeller(SELLER_EXTERNAL_ID));
    }
}