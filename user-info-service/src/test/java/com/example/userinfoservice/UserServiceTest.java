package com.example.userinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import com.yandex.market.userinfoservice.model.NotificationSettings;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
@SqlGroup({
        @Sql(value = "classpath:files/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:files/table-filling.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Test

    public void toster() throws Exception {
        System.out.println(userRepository.findByExternalId(UUID.fromString("f34c4cd3-6fe7-4d3e-b82c-f5d044e46091")).isPresent());

    }

//
//
//    public final String  USER_EXTERNAL_ID = "8400ecc5-0821-4a11-ac70-2165333061dc";
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mvc;
//    @Value("${app.users-api.path}")
//    private String REQUEST_NAME;
//
//    @AfterEach
//    public void resetDb() {
//        userRepository.deleteAll();
//    }
//
//
//    @Test
//    void throwExceptionIfUserNotCreated() throws Exception {
//        UUID uuid = UUID.fromString(USER_EXTERNAL_ID);
//        Mockito.mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(uuid);
////        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
////        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(new User());
//
//        mvc.perform(MockMvcRequestBuilders.post("/public/api/v1/users")
//                .content(Files.readString(Path.of("src/test/resources/files/create-user.json")))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(content().string(USER_EXTERNAL_ID));
//    }
//
//    @Test
//    public void getUserWhenStatus200() throws Exception {
//        System.out.println(userRepository.findByExternalId(UUID.fromString(USER_EXTERNAL_ID)).isPresent());
//        User user = userRepository.findByExternalId(UUID.fromString(USER_EXTERNAL_ID)).get();
//        UUID externalId = user.getExternalId();
//        mvc.perform(get(REQUEST_NAME + "/{externalId}", externalId))
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.deleted").value(false))
//                .andExpect(jsonPath("$.email").value(user.getEmail()))
//                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
//    }
//
//    @Test
//    public void deleteUserWhenStatus200() throws Exception {
//        User user = userRepository.findByExternalId(UUID.fromString(USER_EXTERNAL_ID)).orElseThrow();
//        mvc.perform(delete(REQUEST_NAME + "/{externalId}", user.getExternalId()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getUserWhenStatus400() throws Exception {
//        mvc.perform(get(REQUEST_NAME + "/1"))
//                .andExpect(status().isBadRequest())
//                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
//    }
//
//    @Test
//    public void deleteUserWhenStatus400() throws Exception {
//        mvc.perform(get(REQUEST_NAME + "/1"))
//                .andExpect(status().isBadRequest())
//                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
//    }
//
//    @Test
//    public void getUserWhenStatus404() throws Exception {
//        mvc.perform(get(REQUEST_NAME + "/8b9af550-a4c7-4181-b6ba-1a1899109783"))
//                .andExpect(status().isNotFound())
//                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
//    }
//
//    @Test
//    public void deleteUserWhenStatus404() throws Exception {
//        mvc.perform(get(REQUEST_NAME + "/8b9af550-a4c7-4181-b6ba-1a1899109783"))
//                .andExpect(status().isNotFound())
//                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
//    }
//
//    @BeforeEach
//    public void createTestUser() {
//        NotificationSettings notificationSettings = new NotificationSettings(
//                "test_notificationEmail@gmail.com",
//                false,
//                false,
//                false);
//        User user = User.builder()
//                .externalId(UUID.fromString(USER_EXTERNAL_ID))
//                .firstName("FirstName")
//                .middleName("MiddleName")
//                .lastName("LastName")
//                .phone("89110000000")
//                .email("test_email@gmail.com")
//                .login("Login")
//                .password("password")
//                .birthday(LocalDate.parse("2022-01-01", DateTimeFormatter.ISO_DATE))
//                .sex(Sex.valueOf("MALE"))
//                .isDeleted(false)
//                .notificationSettings(notificationSettings)
//                .build();
//        userRepository.save(user);
//    }
//
}
//
//
