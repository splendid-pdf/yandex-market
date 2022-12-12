package com.market.userinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.userinfoservice.model.NotificationSettings;
import com.market.userinfoservice.model.User;
import com.market.userinfoservice.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserInfoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void resetDb() {
        userRepo.deleteAll();
    }

    @Test
    public void testGetUserWhenStatus200est() throws Exception {
        User user = createTestUser();
        long userId = user.getId();
        mockMvc.perform(get("/public/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
    }

    @Test
    public void testDeleteUserWhenStatus200Test() throws Exception {
        long id = createTestUser().getId();
        User user = userRepo.findById(id).get();
        User userBuffer = copyUser(user);
        mockMvc.perform(delete("/public/api/v1/users/{id}", userBuffer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userBuffer)));
    }

    @Test
    public void testGetUserWhenStatus404Test() throws Exception {
        mockMvc.perform(get("/public/api/v1/users/0"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void testDeleteUserWhenStatus404() throws Exception {
        mockMvc.perform(get("/public/api/v1/users/0"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    private User createTestUser() {
        User user;
        NotificationSettings notificationSettings = new NotificationSettings("test_email",false,false,false);
        user = new User();
        user.setFirstName("TestUser");
        user.setNotificationSettings(notificationSettings);
        return userRepo.save(user);
    }

    private User copyUser(User user) {
        User userBuffer = new User();
        userBuffer.setId(user.getId());
        userBuffer.setFirstName(user.getFirstName());
        userBuffer.setNotificationSettings(user.getNotificationSettings());
        userBuffer.setCreatedAt(user.getCreatedAt());
        userBuffer.setModifiedAt(user.getModifiedAt());
        return userBuffer;
    }

}
