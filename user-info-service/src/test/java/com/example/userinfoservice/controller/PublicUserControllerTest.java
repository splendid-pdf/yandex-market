package com.example.userinfoservice.controller;

import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicUserControllerTest {

    //todo: подумать стоит ли экспешены тестировать тут или лучше в отедльном классе
    //todo: написать модульные тесты
    //todo: глянуть метод expectAll
    //todo: файл наверное тоже может быть как - то можно получше создавать (методы на апдейт и создание)
    //todo: почистить код
    //todo: update contacts добавляет значение а не обновляет сказать мужикам, чтобы исправили или исправить самому
    private static final String DB_FILLING = "classpath:files/sql/db-filling.sql";
    private static final String DB_RESET = "classpath:files/sql/reset.sql";
    private static final String CONTENT_TYPE = "application/json";

    @Value("${spring.test.request.public}")
    private String PUBLIC_REQUEST_NAME;
    @Value("${spring.test.value.existing.external-id}")
    private String EXISTING_EXTERNAL_ID;
    @Value("${spring.test.value.existing.email}")
    private String EXISTING_EMAIL;
    @Value("${spring.test.value.existing.phone}")
    private String EXISTING_PHONE;
    @Value("${spring.test.json.get.expected}")
    private String GET_EXPECTED_RESPONSE;
    @Value("${spring.test.json.create.user}")
    private String CREATE_USER;
    @Value("${spring.test.json.update.user}")
    private String UPDATE_USER;
    @Value("${spring.test.json.update.expected}")
    private String UPDATE_EXPECTED_RESPONSE;

    @Autowired
    private MockMvc mvc;

    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    @Test
    void getUserByExternalIdStatus200() throws Exception {
        File jsonFile = new ClassPathResource(GET_EXPECTED_RESPONSE).getFile();
        mvc.perform(get(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(Files.readString(jsonFile.toPath())));
    }

    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    @Test
    void getUserByEmail200() throws Exception {
        File jsonFile = new ClassPathResource(GET_EXPECTED_RESPONSE).getFile();
        mvc.perform(get(PUBLIC_REQUEST_NAME + "?emailOrPhone=" + EXISTING_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(Files.readString(jsonFile.toPath())));
    }

    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    @Test
    void getUserByPhone200() throws Exception {
        File jsonFile = new ClassPathResource(GET_EXPECTED_RESPONSE).getFile();
        mvc.perform(get(PUBLIC_REQUEST_NAME + "?emailOrPhone=" + EXISTING_PHONE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(Files.readString(jsonFile.toPath())));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void createUserStatus200() throws Exception {
        File jsonFile = new ClassPathResource(CREATE_USER).getFile();
        UUID uuid = UUID.fromString(EXISTING_EXTERNAL_ID);
        try(MockedStatic<UUID> mockedStatic = Mockito.mockStatic(UUID.class)){
            mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
            mvc.perform(post(PUBLIC_REQUEST_NAME)
                            .contentType(APPLICATION_JSON)
                            .content(Files.readString(jsonFile.toPath())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().json("\"" + EXISTING_EXTERNAL_ID + "\""));
        }
    }

    @Test
    @Disabled
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void updateUserStatus200() throws Exception {
        File jsonFileRequest = new ClassPathResource(UPDATE_USER).getFile();
        File jsonFileResponse = new ClassPathResource(UPDATE_EXPECTED_RESPONSE).getFile();
        mvc.perform(put(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFileRequest.toPath())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(Files.readString(jsonFileResponse.toPath())));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void deleteUserStatus204() throws Exception {
        mvc.perform(delete(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID))
                .andExpect(status().isNoContent());
    }
}

