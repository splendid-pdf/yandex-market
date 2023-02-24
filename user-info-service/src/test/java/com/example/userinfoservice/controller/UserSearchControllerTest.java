package com.example.userinfoservice.controller;

import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class UserSearchControllerTest {

    private static final String DB_FILLING = "classpath:files/sql/db-filling.sql";
    private static final String DB_RESET = "classpath:files/sql/reset.sql";
    private static final String CONTENT_TYPE = "application/json";
    @Value("${spring.test.json.filter.user}")
    private String FILTER_USER;
    @Value("${spring.test.json.filter.expected}")
    private String FILTER_EXPECTED_RESPONSE;
    @Value("${spring.test.request.private}")
    private String PRIVATE_REQUEST_NAME;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RedisCacheManager cacheManager;

    @BeforeEach
    void beforeEach() {
        for(String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }

    @Test
    @SqlGroup({
            @Sql(value = DB_FILLING, executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = DB_RESET, executionPhase = AFTER_TEST_METHOD)
    })
    void getUserByFilter() throws Exception {
        File expectedJson = new ClassPathResource(FILTER_EXPECTED_RESPONSE).getFile();
        File requestJson = new ClassPathResource(FILTER_USER).getFile();
            mvc.perform(post(PRIVATE_REQUEST_NAME)
                            .contentType(APPLICATION_JSON)
                            .content(Files.readString(requestJson.toPath())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(CONTENT_TYPE))
                    .andExpect(content().json(Files.readString(expectedJson.toPath())));
    }
}
