package com.yandex.market.reviewservice.mapper;

import com.yandex.market.reviewservice.dto.ReviewDto;
import com.yandex.market.reviewservice.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    Review toReview(ReviewDto reviewDto);

    ReviewDto toReviewDto(Review review);
}
