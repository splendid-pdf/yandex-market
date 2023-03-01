package com.example.userinfoservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import com.yandex.market.userinfoservice.gateway.TwoGisClient;
import com.yandex.market.userinfoservice.mapper.UserRequestMapper;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.model.Location;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.service.UserService;
import com.yandex.market.userinfoservice.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class UserServiceTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
    private UUID uuid;
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private UserResponseMapper userResponseMapper;
    @MockBean
    private UserRequestMapper userRequestMapper;
    @MockBean
    TwoGisClient client;
    @Autowired
    private RedisCacheManager cacheManager;

    @BeforeAll
    static void beforeAll() {
        MAPPER.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void beforeEach() {
        uuid = UUID.fromString(EXISTING_EXTERNAL_ID);
        for(String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }

    @Test
    void findUserByExternalId() throws Exception {
        User user = new User();
        user.setExternalId(uuid);
        File jsonFile = new ClassPathResource(GET_EXPECTED_RESPONSE).getFile();
        UserResponseDto userResponseDtoExpected = MAPPER.readValue(Files.readString(jsonFile.toPath()), UserResponseDto.class);
        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findByExternalId(uuid);
        Mockito.doReturn(userResponseDtoExpected)
                .when(userResponseMapper)
                .map(user);
        UserResponseDto userResponseDtoActual = userService.findUserByExternalId(uuid);
        assertThat(userResponseDtoActual).isEqualTo(userResponseDtoExpected);
    }

    @Test
    void findUserByExternalIdNotFound() throws Exception {
        Mockito.doReturn(Optional.empty())
                .when(userRepository)
                .findByExternalId(uuid);
        assertThrows(EntityNotFoundException.class, () -> userService.findUserByExternalId(uuid));
    }

    @Test
    void deleteUserByExternalId() throws Exception {
        User user = new User();
        user.setExternalId(uuid);
        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findByExternalId(uuid);
        userService.deleteUserByExternalId(uuid);
        Mockito.verify(userRepository, Mockito.times(1)).findByExternalId(ArgumentMatchers.eq(uuid));
    }

    @Test
    void deleteUserByExternalIdNotFound() throws Exception {
        Mockito.doReturn(Optional.empty())
                .when(userRepository)
                .findByExternalId(uuid);
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserByExternalId(uuid));
    }

    @Test
    void update() throws Exception {
        File jsonFileRequest = new ClassPathResource(UPDATE_USER).getFile();
        User updatedUser = new User();
        updatedUser.setEmail(EXISTING_EMAIL);
        updatedUser.setPhone(EXISTING_PHONE);
        User storedUser = new User();
        storedUser.setEmail(EXISTING_EMAIL);
        storedUser.setPhone(EXISTING_PHONE);
        File jsonFileResponse = new ClassPathResource(GET_EXPECTED_RESPONSE).getFile();
        UserResponseDto userResponseDtoExpected = MAPPER.readValue(Files.readString(jsonFileResponse.toPath()), UserResponseDto.class);
        MAPPER.registerModule(new JavaTimeModule());
        UserRequestDto userRequestDto = MAPPER.readValue(Files.readString(jsonFileRequest.toPath()), UserRequestDto.class);
        Mockito.doNothing()
                .when(userValidator)
                .validate(userRequestDto);
        Mockito.doReturn(Optional.of(storedUser))
                .when(userRepository)
                .findByExternalId(uuid);
        Mockito.doReturn(updatedUser)
                .when(userRequestMapper)
                .map(userRequestDto);
        Mockito.doReturn(userResponseDtoExpected)
                .when(userResponseMapper)
                .map(updatedUser);
        UserResponseDto userResponseDtoActual = userService.update(uuid, userRequestDto);
        assertThat(userResponseDtoActual).isEqualTo(userResponseDtoExpected);
    }

    @Test
    void updateIllegalNotFound() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        Mockito.doNothing()
                .when(userValidator)
                .validate(userRequestDto);
        Mockito.doReturn(Optional.empty())
                .when(userRepository)
                .findByExternalId(uuid);
        assertThrows(EntityNotFoundException.class, () -> userService.update(uuid, userRequestDto));
    }

    private Location createLocation() {
        Location location = new Location();
        location.setCity("Saint-Petersburg");
        location.setCountry("Russia");
        location.setRegion("Florida");
        location.setStreet("Ocean Drive");
        location.setHouseNumber("5");
        location.setApartmentNumber("12");
        location.setPostcode("192281");
        location.setLatitude(1.0);
        location.setLongitude(1.0);
        return location;
    }

}
