package com.yandex.market.reviewservice.controller;

import com.yandex.market.reviewservice.dto.ReviewDto;
import com.yandex.market.reviewservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${spring.application.url}")
@RequiredArgsConstructor
@Tag(name = "Методы review-service для работы с сущностью \"Отзыв\"")
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/reviews")
    @Operation(summary = "Создание нового отзыва", responses = {
            @ApiResponse(description = "Новый отзыв создан", responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(description = "Ошибка при создании нового отзыва", responseCode = "400")
    })
    public UUID createReview(@RequestBody @Valid ReviewDto reviewDto,
                             @Parameter(name = "userId", description = "Индентификатор пользователя")
                             @PathVariable("userId") UUID userId) {
        log.info("Received a request to create new review {} for user: {}}", reviewDto, userId);
        return reviewService.create(reviewDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}/reviews")
    @Operation(summary = "Получение всех отзывов пользователя")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public Page<ReviewDto> getReviewsByUserId(
            @Parameter(name = "userId", description = "Индентификатор пользователя")
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "creationTimestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received a request to get reviews by user identifier: {}", userId);
        return reviewService.getReviewsByUserId(userId, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/{productId}/reviews")
    @Operation(summary = "Получение всех отзывов о продукте")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public Page<ReviewDto> getReviewsByProductId(
            @Parameter(name = "productId", description = "Индентификатор продукта")
            @PathVariable("productId") UUID productId,
            @PageableDefault(sort = "creationTimestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received a request to get reviews by product identifier: {}", productId);
        return reviewService.getReviewsByProductExternalId(productId, pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/reviews/{reviewId}")
    @Operation(summary = "Изменение отзыва", responses = {
            @ApiResponse(description = "Отзыв успешно изменен", responseCode = "204",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(description = "Такого отзыва не существует", responseCode = "404")
    })
    public ReviewDto updateReview(
            @Parameter(name = "reviewId", description = "Индентификатор отзыва")
            @PathVariable("reviewId") UUID reviewExternalId,
            @RequestBody @Valid ReviewDto reviewDto) {
        log.info("Received a request to update an review: {}", reviewExternalId);
        return reviewService.update(reviewDto, reviewExternalId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/reviews/{reviewId}")
    @Operation(summary = "Удаление отзыва", responses = {
            @ApiResponse(description = "Отзыв успешно удален", responseCode = "204"),
            @ApiResponse(description = "Такого отзыва не существует", responseCode = "404")
    })
    public void deleteReview(
            @Parameter(name = "reviewId", description = "Индентификатор отзыва")
            @PathVariable("reviewId") UUID reviewExternalId) {
        log.info("Received a request to delete an review: {}", reviewExternalId);
        reviewService.delete(reviewExternalId);
    }
}