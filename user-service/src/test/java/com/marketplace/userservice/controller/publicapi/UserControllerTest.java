package com.marketplace.userservice.controller.publicapi;

import com.marketplace.userservice.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.yandex.market.auth.util.AuthUtils.token;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenCreatingUser_thenSucceedAndIdIsReturned() throws Exception {
        UUID uuid = UUID.randomUUID();
        try (MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class)) {
            mockedUuid.when(UUID::randomUUID).thenReturn(uuid);

            mockMvc.perform(
                            MockMvcRequestBuilders.post("/public/api/v1/users")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "email": "testuser@mail.ru",
                                                    "password": "!Password1"
                                                }
                                             """
                                    )
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(uuid.toString()));
        }
    }

    @Test
    void whenCreatingUserAndUserWithSuchEmailHadAlreadyCreated_thenThrowException() throws Exception {
        UUID uuid = UUID.randomUUID();
        OffsetDateTime dateTime = OffsetDateTime.parse("2023-04-23T18:15:30+03:00");
        try (
                MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class);
                MockedStatic<OffsetDateTime> mockedOffsetDateTime = Mockito.mockStatic(OffsetDateTime.class)
        ) {
            mockedUuid.when(UUID::randomUUID).thenReturn(uuid);
            mockedOffsetDateTime.when(OffsetDateTime::now).thenReturn(dateTime);

            mockMvc.perform(
                            MockMvcRequestBuilders.post("/public/api/v1/users")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "email": "test@mail.ru",
                                                    "password": "!Password1"
                                                }
                                             """
                                    ))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.id").value(uuid.toString()))
                    .andExpect(jsonPath("$.message").value("User with similar email = test@mail.ru is already exists"))
                    .andExpect(jsonPath("$.timestamp").value(dateTime.format(DateTimeFormatter.ISO_DATE_TIME)));
        }
    }

    @Test
    void whenGettingUserWithoutToken_thenExpectUnauthorizedStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/public/api/v1/users/f34c4cd3-6fe7-4d3e-b82c-f5d044e46091")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenGettingUserWithTokenWhichHoldsOtherUserId_thenExpectForbiddenStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/public/api/v1/users/f34c4cd3-6fe7-4d3e-b82c-f5d044e46091")
                                .with(authentication(token("t51c4cd3-6fe7-4d3e-b82c-f5d044e46091", "ROLE_USER")))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminWantsToGetInfoAboutAnyUser_thenOk() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/public/api/v1/users/f34c4cd3-6fe7-4d3e-b82c-f5d044e46091")
                                .with(authentication(token("t51c4cd3-6fe7-4d3e-b82c-f5d044e46091", "ROLE_ADMIN")))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                                        {
                                                          "id": "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091",
                                                          "firstName": "John",
                                                          "lastName": "Doe",
                                                          "phone": "+79216437711",
                                                          "email": "test@mail.ru",
                                                          "sex": "MALE",
                                                          "photoId": "id12",
                                                          "location": {
                                                            "city": "Moscow",
                                                            "deliveryAddress": "Tverskaya str, 34"
                                                          }
                                                        }
                                                    """
                ));
    }

    @Test
    void whenUpdatingUser_thenOk() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/public/api/v1/users/f34c4cd3-6fe7-4d3e-b82c-f5d044e46091")
                                .with(authentication(token("f34c4cd3-6fe7-4d3e-b82c-f5d044e46091", "ROLE_USER")))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                        {
                                                          "id": "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091",
                                                          "firstName": "Johny",
                                                          "lastName": "Doeeeeee",
                                                          "phone": "+79216431722",
                                                          "email": "test15@mail.ru",
                                                          "sex": "MALE",
                                                          "photoId": "id16",
                                                          "location": {
                                                            "city": "Leningrad",
                                                            "deliveryAddress": "Nevskii avenue, 35"
                                                          }
                                                        }
                                         """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                                        {
                                                          "id": "f34c4cd3-6fe7-4d3e-b82c-f5d044e46091",
                                                          "firstName": "Johny",
                                                          "lastName": "Doeeeeee",
                                                          "phone": "+79216431722",
                                                          "email": "test15@mail.ru",
                                                          "sex": "MALE",
                                                          "photoId": "id16",
                                                          "location": {
                                                            "city": "Leningrad",
                                                            "deliveryAddress": "Nevskii avenue, 35"
                                                          }
                                                        }
                                                    """
                ));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/public/api/v1/users/f34c4cd3-6fe7-4d3e-b82c-f5d044e46091")
                        .with(authentication(token("f34c4cd3-6fe7-4d3e-b82c-f5d044e46091", "ROLE_USER"))))
                .andExpect(status().isNoContent());
    }
}