package com.example.userinfoservice.repository;

import com.yandex.market.userservice.UserServiceApplication;
import com.yandex.market.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@SpringBootTest(classes = UserServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
@SqlGroup({
        @Sql(value = "classpath:files/sql/db-filling.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:files/sql/reset.sql", executionPhase = AFTER_TEST_METHOD)
})
class UserRepositoryTest {

    @Value("${spring.test.value.existing.external-id}")
    private String EXISTING_EXTERNAL_ID;
    @Value("${spring.test.value.existing.email}")
    private String EXISTING_EMAIL;
    @Value("${spring.test.value.existing.phone}")
    private String EXISTING_PHONE;
    @Value("${spring.test.value.not-existing.external-id}")
    private String NOT_EXISTING_EXTERNAL_ID;
    @Value("${spring.test.value.not-existing.phone}")
    private String NOT_EXISTING_PHONE;
    @Value("${spring.test.value.not-existing.email}")
    private String NOT_EXISTING_EMAIL;

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail() {
        assertAll(
                () -> assertThat(userRepository.existsByEmail(EXISTING_EMAIL)).isTrue(),
                () -> assertThat(userRepository.existsByEmail(NOT_EXISTING_EMAIL)).isFalse()
        );

    }

    @Test
    void findByExternalId() {
        assertAll(
                () -> assertThat(userRepository.findByExternalId(UUID.fromString(EXISTING_EXTERNAL_ID))).isPresent(),
                () -> assertThat(userRepository.findByExternalId(UUID.fromString(NOT_EXISTING_EXTERNAL_ID))).isNotPresent()
        );

    }

    @Test
    void deleteUserByExternalId() {
        //todo он void подумать как его проверить
    }

    @Test
    void findUserByEmail() {
        assertAll(
                () -> assertThat(userRepository.findUserByEmail(EXISTING_EMAIL)).isPresent(),
                () -> assertThat(userRepository.findUserByEmail(NOT_EXISTING_EMAIL)).isNotPresent()
        );
    }

    @Test
    void findUserByPhone() {
        assertAll(
                () -> assertThat(userRepository.findUserByPhone(EXISTING_PHONE)).isPresent(),
                () -> assertThat(userRepository.findUserByPhone(NOT_EXISTING_PHONE)).isNotPresent()
        );

    }

}
