package com.example.userinfoservice.service;

import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.service.UserSearchService;
import com.yandex.market.userinfoservice.specification.UserSpecification;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class UserSearchServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserSpecification userSpecification;
    @MockBean
    private UserResponseMapper userResponseMapper;
    @Autowired
    private UserSearchService userSearchService;
//todo доделай метод
    @Test
    void getUsersByFilter() throws Exception {
        UserFilter userFilter = new UserFilter();
        User user = new User();
        List<User> usersList = new ArrayList<>(1);
        usersList.add(user);
        Mockito.doReturn(null)
                .when(userSpecification)
                .getSpecificationFromUserFilter(userFilter);
        Mockito.doReturn(usersList)
                .when(userRepository)
                .findAll();
        Mockito.doReturn(null)
                .when(userResponseMapper)
                .map(user);
        List<UserResponseDto> userResponseDtoList = userSearchService.getUsersByFilter(userFilter);
        assertThat(userResponseDtoList).isEqualTo(Collections.EMPTY_LIST);

    }
}
