package com.market.userinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.userinfoservice.model.NotificationSettings;
import com.market.userinfoservice.model.User;
import com.market.userinfoservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserInfoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
    }

    @Test
    public void getUserWhenStatus200() throws Exception {
        User user = createTestUser();
        UUID externalId = user.getExternalId();
        mockMvc.perform(get("/public/api/v1/users/{externalId}", externalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.externalId").value(externalId.toString()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
    }

    @Test
    public void deleteUserWhenStatus200() throws Exception {
        UUID externalId = createTestUser().getExternalId();
        User user = userRepository.findByExternalId(externalId).orElseThrow();
        mockMvc.perform(delete("/public/api/v1/users/{externalId}", user.getExternalId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserWhenStatus400() throws Exception {
        mockMvc.perform(get("/public/api/v1/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void deleteUserWhenStatus400() throws Exception {
        mockMvc.perform(get("/public/api/v1/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void getUserWhenStatus404() throws Exception {
        mockMvc.perform(get("/public/api/v1/users/8b9af550-a4c7-4181-b6ba-1a1899109783"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void deleteUserWhenStatus404() throws Exception {
        mockMvc.perform(get("/public/api/v1/users/8b9af550-a4c7-4181-b6ba-1a1899109783"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    private User createTestUser() {
        User user = new User();
        NotificationSettings notificationSettings = new NotificationSettings("test_email",false,false,false);
        user.setFirstName("TestUser");
        user.setDeleted(false);
        user.setNotificationSettings(notificationSettings);
        user.setExternalId(UUID.fromString("8b9af550-a4c7-4181-b6ba-1a1899109783"));
        return userRepository.save(user);
    }

}
