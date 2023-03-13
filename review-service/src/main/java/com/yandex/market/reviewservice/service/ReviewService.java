package com.yandex.market.reviewservice.service;

import com.yandex.market.reviewservice.dto.ReviewDto;
import com.yandex.market.reviewservice.mapper.ReviewMapper;
import com.yandex.market.reviewservice.model.Review;
import com.yandex.market.reviewservice.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public UUID create(ReviewDto reviewDto, UUID userId) {
        Review review = reviewMapper.toReview(reviewDto);
        review.setUserId(userId);
        return reviewRepository.save(review).getExternalId();
    }

    @Transactional
    public ReviewDto update(ReviewDto reviewDto, UUID reviewExternalId) {
        Review storedReview = reviewRepository.getReviewByExternalId(reviewExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found by external id = '%s'"
                        .formatted(reviewExternalId)));
        Review review = reviewMapper.toReview(reviewDto);
        review.setId(storedReview.getId());
        review.setUserId(storedReview.getUserId());
        review.setExternalId(storedReview.getExternalId());
        review.setCreationTimestamp(storedReview.getCreationTimestamp());
        review.setUpdateTimestamp(LocalDateTime.now());
        return reviewMapper.toReviewDto(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviewsByUserId(UUID userId, Pageable pageable) {
        Page<Review> pagedResult = reviewRepository.getReviewsByUserId(userId, pageable);

        return new PageImpl<>(pagedResult.getContent()
                .stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviewsByProductExternalId(UUID productId, Pageable pageable) {
        Page<Review> pagedResult = reviewRepository.getReviewsByProductId(productId, pageable);

        return new PageImpl<>(pagedResult.getContent()
                .stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void delete(UUID reviewExternalId) {
        Review review = reviewRepository.getReviewByExternalId(reviewExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found by external id = '%s'"
                        .formatted(reviewExternalId)));
        reviewRepository.deleteById(review.getId());
    }

    public ReviewDto getByExternalId(UUID reviewExternalId) {
        return reviewMapper.toReviewDto(reviewRepository.getReviewByExternalId(reviewExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found by external id = '%s'"
                        .formatted(reviewExternalId))));
    }
}