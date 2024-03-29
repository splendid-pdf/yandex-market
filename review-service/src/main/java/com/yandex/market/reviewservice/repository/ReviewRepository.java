package com.yandex.market.reviewservice.repository;

import com.yandex.market.reviewservice.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> getReviewsByUserId(UUID userId, Pageable pageable);

    Page<Review> getReviewsByProductId(UUID productId, Pageable pageable);

    Optional<Review> getReviewByExternalId(UUID reviewExternalId);
}
