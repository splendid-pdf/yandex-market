package com.yandex.market.basketservice;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
@TestPropertySource(locations = "classpath:application-testcontainers.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractTestIntegration {

    private static final String IMAGE_NAME = "postgres:15.2-alpine";
    private static final String SQL_INIT = "files/sql/init.sql";

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>(IMAGE_NAME)
                    .withUsername("user")
                    .withPassword("password")
                    .withDatabaseName("product_db")
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource(SQL_INIT),
                            "/docker-entrypoint-initdb.d/"
                    );

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.user", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }
}
