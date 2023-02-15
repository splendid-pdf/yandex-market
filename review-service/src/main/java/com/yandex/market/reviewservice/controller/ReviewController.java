package com.yandex.market.reviewservice.controller;

import com.yandex.market.reviewservice.dto.ReviewDto;
import com.yandex.market.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/reviews")
    public UUID createOrder(@RequestBody ReviewDto reviewDto,
                            @PathVariable("userId") UUID userId) {
        log.info("Received a request to create new review {} for user: {}}", reviewDto, userId);
        return reviewService.create(reviewDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}/reviews")
    public Page<ReviewDto> getReviewsByUserId(
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received a request to get reviews by user identifier: {}", userId);
        return reviewService.getReviewsByUserId(userId, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/{productId}/reviews")
    public Page<ReviewDto> getReviewsByProductId(
            @PathVariable("productId") UUID productId,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received a request to get reviews by product identifier: {}", productId);
        return reviewService.getReviewsByProductExternalId(productId, pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/reviews/{reviewId}")
    public ReviewDto update(@RequestBody ReviewDto reviewDto,
                            @PathVariable("reviewId") UUID reviewExternalId) {
        log.info("Received a request to update an review: {}", reviewExternalId);
        return reviewService.update(reviewDto, reviewExternalId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/reviews/{reviewId}")
    public void delete(@PathVariable("reviewId") UUID reviewExternalId) {
        log.info("Received a request to delete an review: {}", reviewExternalId);
        reviewService.delete(reviewExternalId);
    }
}