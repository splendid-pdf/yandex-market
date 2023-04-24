package com.marketplace.userservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.marketplace.userservice.dto.response.UserResponseDto;
import com.marketplace.userservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class UserResponseMapper implements Mapper<User, UserResponseDto> {
    private final LocationMapper locationMapper;

    @Override
    public UserResponseDto map(User user) {
        return new UserResponseDto(
                user.externalId(),
                user.firstName(),
                user.lastName(),
                user.phone(),
                user.email(),
                nonNull(user.sex()) ? user.sex().name() : null,
                user.photoId(),
                nonNull(user.location()) ? locationMapper.mapToDto(user.location()) : null
        );
    }
}