package com.example.userinfoservice.controller;

import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class UserInfoExceptionHandlerTest {
    private final static String DB_FILLING = "classpath:files/sql/db-filling.sql";
    private final static String DB_RESET = "classpath:files/sql/reset.sql";

    @Value("${spring.test.request.public}")
    private String PUBLIC_REQUEST_NAME;
    @Value("${spring.test.value.existing.external-id}")
    private String EXISTING_EXTERNAL_ID;
    @Value("${spring.test.value.not-existing.external-id}")
    private String NOT_EXISTING_EXTERNAL_ID;
    @Value("${spring.test.value.not-existing.phone}")
    private String NOT_EXISTING_PHONE;
    @Value("${spring.test.json.wrong}")
    private String WRONG_JSON;
    @Value("${spring.test.json.create.wrong-birthday}")
    private String CREATE_WRONG_BIRTHDAY;
    @Value("${spring.test.json.create.not-valid}")
    private String CREATE_NOT_VALID;
    @Value("${spring.test.json.update.wrong-birthday}")
    private String UPDATE_WRONG_BIRTHDAY;
    @Value("${spring.test.json.update.not-valid}")
    private String UPDATE_NOT_VALID;
    @Value("${spring.test.json.update.user}")
    private String UPDATE_USER;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RedisCacheManager cacheManager;

    @BeforeEach
    void beforeEach() {
        for(String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void getUserValidationExceptionStatus400() throws Exception {
        mvc.perform(get(PUBLIC_REQUEST_NAME + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(ValidationException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void getUserEntityNotFoundExceptionStatus404() throws Exception {
        mvc.perform(get(PUBLIC_REQUEST_NAME + "/" + NOT_EXISTING_EXTERNAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void getUserByEmailOrPhoneNotFoundExceptionStatus404() throws Exception {
        mvc.perform(get(PUBLIC_REQUEST_NAME + "?emailOrPhone=" + NOT_EXISTING_PHONE))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void deleteUserValidationExceptionStatus400() throws Exception {
        mvc.perform(delete(PUBLIC_REQUEST_NAME + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(ValidationException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void deleteUserEntityNotFoundExceptionStatus404() throws Exception {
        mvc.perform(delete(PUBLIC_REQUEST_NAME + "/" + NOT_EXISTING_EXTERNAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void createUserWrongJsonStatus400() throws Exception {
        File jsonFile = new ClassPathResource(WRONG_JSON).getFile();
        mvc.perform(post(PUBLIC_REQUEST_NAME)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(HttpMessageNotReadableException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void createUserValidationExceptionStatus400() throws Exception {
        File jsonFile = new ClassPathResource(CREATE_NOT_VALID).getFile();
        mvc.perform(post(PUBLIC_REQUEST_NAME)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(ValidationException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void createUserDateTimeParseExceptionStatus400() throws Exception {
        File jsonFile = new ClassPathResource(CREATE_WRONG_BIRTHDAY).getFile();
        mvc.perform(post(PUBLIC_REQUEST_NAME)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(DateTimeParseException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void updateUserWrongJsonStatus400() throws Exception {
        File jsonFile = new ClassPathResource(WRONG_JSON).getFile();
        mvc.perform(put(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(HttpMessageNotReadableException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void updateUserDateTimeParseExceptionStatus400() throws Exception {
        File jsonFile = new ClassPathResource(UPDATE_WRONG_BIRTHDAY).getFile();
        mvc.perform(put(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(DateTimeParseException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void updateUserValidationExceptionStatus400() throws Exception {
        File jsonFile = new ClassPathResource(UPDATE_NOT_VALID).getFile();
        mvc.perform(put(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(ValidationException.class));
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void updateUserEntityNotFoundExceptionStatus404() throws Exception {
        File jsonFile = new ClassPathResource(UPDATE_USER).getFile();
        mvc.perform(put(PUBLIC_REQUEST_NAME + "/{externalId}", NOT_EXISTING_EXTERNAL_ID)
                        .contentType(APPLICATION_JSON)
                        .content(Files.readString(jsonFile.toPath())))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> Objects.requireNonNull(mvcResult.getResolvedException())
                        .getClass()
                        .equals(EntityNotFoundException.class));
    }

}
