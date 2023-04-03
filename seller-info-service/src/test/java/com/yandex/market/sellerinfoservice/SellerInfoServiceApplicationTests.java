package com.yandex.market.sellerinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yandex.market.exception.ValidationException;
import com.yandex.market.sellerinfoservice.dto.SellerRegistration;
import com.yandex.market.sellerinfoservice.service.SellerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testcontainers")
@TestPropertySource(locations = "classpath:application-testcontainers.yml")
class SellerInfoServiceApplicationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SellerService sellerService;
    private static final String CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON = "src/test/resources/json/create/create_seller_request_dto_negative.json";

    private final static String TOKEN = "Bearer eyJraWQiOiJkZjIzNDY0Yy01NzlmLTQ2ZTQtYmZlOS0yOTZhMjk0MDEzZTEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbGV4QG1haWwucnUiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2ODA1MjY4MjIsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOi8vNTEuMjUwLjEwMi4xMjo5MDAwIiwiZXhwIjoxNjgwNzQyODIyLCJpYXQiOjE2ODA1MjY4MjIsInNlbGxlci1pZCI6ImRjMWQzODE0LTQ5ZGUtNGEzYS1iNmFjLTliY2ZjOTg2ZGZlMiIsImF1dGhvcml0aWVzIjpbIlJPTEVfU0VMTEVSIl19.GfsYPtbILVISgXLH4egQltibKX0dwR7DpkJZ6zsvYNvN7uwKd4AMF2rsIynyYDGip4k6HX0uxqp00MVbdhfgYTOfdcHE2or7qsZGA6NfeiTFLZyPZtZnK5ZhGrfcmnN3gw2GOalKRPlTFhkAbQYGSJfxdG7SD3_3bahO8yF7-a7mtKKTK3lh3TVHNcYgbuYIVJ0i3H0v5r0FsTZeEo1GLnxcSJl7jb6v2JewG-ofP3ADQ0rVQ9I3PQkMdcg6O_LXmuvYg9_I_H5N7fWfav8EhjdGWw9C2DOu5scyUh7hdfoTSPPgRRPI0_Ra9FboQCJSIhvrvc_-P2XcrgI7CRXi6w";

    private static final String CREATE_SELLER_REQUEST_DTO_JSON = "src/test/resources/json/create/create_seller_request_dto.json";
    private final UUID SELLER_EXTERNAL_ID = UUID.fromString("47678201-f3c8-4d5c-a628-2344eef50c54");
    private final UUID SELLER_EXTERNAL_ID_NEGATIVE = UUID.fromString("77678201-f3c8-4d5c-a628-2344eef50c55");
    @Value("${spring.app.seller.url}")
    private String SELLERS_URL;

    @Test
    @Transactional
    void createSellerController() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SELLERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON))))
                .andExpect(status().isCreated())
                .andReturn();

        String expectedEmail = "alex@mail.ru";
        UUID actualSellerExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);
        String email = sellerService.getSellerByExternalId(actualSellerExternalId).email();

        Assertions.assertNotNull(sellerService.getSellerByExternalId(actualSellerExternalId));
        Assertions.assertEquals(expectedEmail, email);
    }

    @Test
    @Transactional
    void createSellerNegativeController() throws Exception {
        mockMvc.perform(post(SELLERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_NEGATIVE_JSON))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Sql("/db/insert_test_seller.sql")
    void createSellerNegativeControllerExistException() throws Exception {
        mockMvc.perform(post(SELLERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON))))
                .andExpect(status().isConflict());

        Assertions.assertThrows(EntityExistsException.class, () -> sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRegistration.class)));
    }

    @Test
    @Transactional
    void createSellerService() throws IOException {
        UUID actualSellerExternalId = sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRegistration.class));
        Assertions.assertNotNull(sellerService.getSellerByExternalId(actualSellerExternalId));
    }

    @Test
    @Transactional
    @Sql("/db/insert_test_seller.sql")
    void createSellerServiceNegativeServiceExistException() {
        Assertions.assertThrows(EntityExistsException.class, () -> sellerService
                .createSeller(objectMapper.readValue(Files.readString(Path.of(CREATE_SELLER_REQUEST_DTO_JSON)), SellerRegistration.class)));
    }

//    @Test
//    @Transactional
//    @Sql("/db/insert_test_seller.sql")
//    void deleteSellerController() throws Exception {
//        mockMvc.perform(delete(SELLERS_URL + "/" + SELLER_EXTERNAL_ID)
//                        .header(AUTHORIZATION, TOKEN))
//                .andExpect(status().isOk());
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
//                .deleteSellerByExternalId(SELLER_EXTERNAL_ID));
//    }

    @Test
    @Transactional
    void deleteSellerControllerNegative() throws Exception {
        mockMvc.perform(delete(SELLERS_URL + SELLER_EXTERNAL_ID_NEGATIVE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @Sql("/db/insert_test_seller.sql")
    void deleteSellerService() {
        sellerService.deleteSellerByExternalId(SELLER_EXTERNAL_ID);

        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .getSellerByExternalId(SELLER_EXTERNAL_ID));
    }

    @Test
    @Transactional
    void deleteSellerServiceNegative() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> sellerService
                .deleteSellerByExternalId(SELLER_EXTERNAL_ID));
    }
}