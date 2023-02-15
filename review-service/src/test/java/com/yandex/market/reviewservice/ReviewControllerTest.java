package com.yandex.market.reviewservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.reviewservice.dto.ReviewDto;
import com.yandex.market.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("testcontainers")
public class ReviewControllerTest {

    private final MockMvc mockMvc;

    private final ReviewService reviewService;

    private final ObjectMapper objectMapper;

    @Test
    @Transactional
    public void create() throws Exception {
        UUID userId = UUID.fromString("cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04");

        MvcResult mvcResult = mockMvc.perform(post("/public/api/v1/users/{userId}/reviews", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateReviewDto.json"))))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualReviewExternalId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UUID.class);

        Assertions.assertNotNull(reviewService.getByExternalId(actualReviewExternalId));
    }

    @Test
    @Sql("/db/insertTestReview.sql")
    @Transactional
    public void getReviewsByUserId() throws Exception {
        UUID userId = UUID.fromString("ed39e6e1-bf4a-4e77-b29a-69cd82bfc516");
        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/users/{userId}/reviews", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ReviewDto> reviewDtoPage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ReviewDto>>() {
        });

        Assertions.assertEquals(2L, reviewDtoPage.getTotalElements());
        Assertions.assertEquals(1L, reviewDtoPage.getTotalPages());
        Assertions.assertEquals(4, reviewDtoPage.getContent().get(0).score());
        Assertions.assertEquals(5, reviewDtoPage.getContent().get(1).score());
    }

    @Test
    @Sql("/db/insertTestReview.sql")
    @Transactional
    public void getReviewsByProductId() throws Exception {
        UUID productId = UUID.fromString("5428bd51-996c-4ddf-a97d-57855203720d");
        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/products/{productId}/reviews", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<ReviewDto> reviewDtoPage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestPageImpl<ReviewDto>>() {
        });

        Assertions.assertEquals(2L, reviewDtoPage.getTotalElements());
        Assertions.assertEquals(1L, reviewDtoPage.getTotalPages());
        Assertions.assertEquals(4, reviewDtoPage.getContent().get(0).score());
        Assertions.assertEquals(5, reviewDtoPage.getContent().get(1).score());
    }

    @Test
    @Transactional
    @Sql("/db/insertTestReview.sql")
    public void update() throws Exception {
        UUID reviewId = UUID.fromString("9728bd51-996c-4ddf-a97d-57855203720d");
        UUID userId = UUID.fromString("9728bd51-996c-4ddf-a97d-57855203720d");

        MvcResult mvcResult = mockMvc
                .perform(put("/public/api/v1/users/reviews/{reviewId}", userId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readString(Path.of("src/test/resources/CreateReviewDtoUpdate.json"))))
                .andExpect(status().isNoContent())
                .andReturn();

        ReviewDto actualReviewDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReviewDto.class);
        int actualScore = actualReviewDto.score();
        Assertions.assertEquals(2, actualScore);
    }

    @Test
    @Sql("/db/insertTestReview.sql")
    @Transactional
    public void delete() throws Exception {
        UUID reviewId = UUID.fromString("9728bd51-996c-4ddf-a97d-57855203720d");

        mockMvc.perform(MockMvcRequestBuilders.delete("/public/api/v1/users/reviews/{reviewId}", reviewId))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertNull(reviewService.getByExternalId(reviewId));
    }

    static class RestPageImpl<T> extends PageImpl<T> {

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public RestPageImpl(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {

            super(content, PageRequest.of(number, size), totalElements);
        }

        public RestPageImpl(List<T> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }

        public RestPageImpl(List<T> content) {
            super(content);
        }

        public RestPageImpl() {
            super(new ArrayList<>());
        }
    }
}