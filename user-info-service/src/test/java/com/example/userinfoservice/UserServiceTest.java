package com.example.userinfoservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class UserServiceTest {

    //todo: поискать аннотации для тестирования рест контроллеров
    //todo: переиминовать класс в какой - нибудь UserServiceControllerTest
    //todo: написать тест для поиска по телефону/емайлу и фильтрации и экспешенов
    //todo: подумать стоит ли экспешены тестировать тут или лучше в отедльном классе
    //todo: заавтавайрить маппер в тесте на апдейт
    //todo: класс для модульных тестов, написать модульные тесты для сервиса / репозитория / еще может быть чего-то
    //todo: глянуть метод expectAll
    //todo: файл наверное тоже может быть как - то можно получше создавать ( методы на апдейт и создание)
    //todo: почистить код
    //todo разобраться почему в тесте создания Mockito.mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(EXISTING_EXTERNAL_ID); ломает все тесты


    public final static UUID EXISTING_EXTERNAL_ID = UUID.fromString("f34c4cd3-6fe7-4d3e-b82c-f5d044e46091");
    public final static UUID NOT_EXISTING_EXTERNAL_ID = UUID.fromString("f34c4cd3-6fe7-4d3e-b82c-f5d044e46092");
    public final static String PRIVATE_REQUEST_NAME = "/private/api/v1/users";
    public final static String PUBLIC_REQUEST_NAME = "/public/api/v1/users";

    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;

//    @Test
//    void throwExceptionIfUserNotCreated() throws Exception {
//        UUID uuid = EXISTING_EXTERNAL_ID;
//        Mockito.mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(uuid);
//        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
//        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(new User());
//
//        mvc.perform(MockMvcRequestBuilders.post("/public/api/v1/users")
//                .content(Files.readString(Path.of("src/test/resources/files/create-user.json")))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(uuid.toString()));
//    }

    @SqlGroup({
            @Sql(value = "classpath:files/table-filling.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:files/reset.sql", executionPhase = AFTER_TEST_METHOD)
    })
    @Test
    void getUserWhenStatus200() throws Exception {
        User user = userRepository.findByExternalId(EXISTING_EXTERNAL_ID).get();
        mvc.perform(get(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:files/reset.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void createUserWhenStatus200() throws Exception {
        final File jsonFile = new ClassPathResource("files/create-user.json").getFile();
        final String userToCreate = Files.readString(jsonFile.toPath());
//        Mockito.mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(EXISTING_EXTERNAL_ID);
        mvc.perform(post(PUBLIC_REQUEST_NAME)
                        .contentType(APPLICATION_JSON)
                        .content(userToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
//                .andExpect(content().string("\"" + EXISTING_EXTERNAL_ID + "\""));
//                .andReturn();
//        assertThat(mvcResult.getResponse().getContentAsString().replaceAll("\"{2}","\"")).isEqualTo(EXISTING_EXTERNAL_ID.toString());
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:files/table-filling.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:files/reset.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void updateUserWhenStatus200() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        final File jsonFile = new ClassPathResource("files/update-user.json").getFile();
        final String userToUpdate = Files.readString(jsonFile.toPath());
        JsonNode jsonNode = objectMapper.readTree(userToUpdate);
        mvc.perform(put(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID)
                        .contentType(APPLICATION_JSON)
                        .content(userToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.firstName").value(jsonNode.get("firstName").asText()))
                .andExpect(jsonPath("$.middleName").value(jsonNode.get("middleName").asText()))
                .andExpect(jsonPath("$.lastName").value(jsonNode.get("lastName").asText()))
                .andExpect(jsonPath("$.email").value(jsonNode.get("email").asText()));
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:files/table-filling.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:files/reset.sql", executionPhase = AFTER_TEST_METHOD)
    })
    void deleteUserWhenStatus204() throws Exception {
        User user = userRepository.findByExternalId(EXISTING_EXTERNAL_ID).orElseThrow();
        mvc.perform(delete(PUBLIC_REQUEST_NAME + "/{externalId}", EXISTING_EXTERNAL_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserWhenStatus400() throws Exception {
        mvc.perform(get(PUBLIC_REQUEST_NAME + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    void deleteUserWhenStatus400() throws Exception {
        mvc.perform(delete(PUBLIC_REQUEST_NAME + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    void getUserWhenStatus404() throws Exception {
        mvc.perform(get(PUBLIC_REQUEST_NAME + "/" + NOT_EXISTING_EXTERNAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    void deleteUserWhenStatus404() throws Exception {
        mvc.perform(delete(PUBLIC_REQUEST_NAME + "/" + NOT_EXISTING_EXTERNAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

}

