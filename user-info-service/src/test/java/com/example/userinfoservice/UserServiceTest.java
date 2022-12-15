package com.example.userinfoservice;

import com.market.userinfoservice.UserInfoServiceApplication;
import com.market.userinfoservice.model.User;
import com.market.userinfoservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
public class UserServiceTest {


    public static final String USER_EXTERNAL_ID = "8400ecc5-0821-4a11-ac70-2165333061dc";
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void throwExceptionIfUserNotCreated() throws Exception {
        UUID uuid = UUID.fromString(USER_EXTERNAL_ID);
        Mockito.mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(uuid);
        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(new User());

        mvc.perform(MockMvcRequestBuilders.post("/public/api/v1/users")
                .content(Files.readString(Path.of("src/test/resources/files/create-user.json"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));
    }

}
