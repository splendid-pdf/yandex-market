package com.yandex.market.userservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userservice.dto.response.UserResponseDto;
import com.yandex.market.userservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserResponseMapper implements Mapper<User, UserResponseDto> {
    private final LocationMapper locationMapper;

    @Override
    public UserResponseDto map(User user) {
        val sex = user.getSex() != null ? user.getSex().name() : null;
        val locationDto = user.getLocation() != null ? locationMapper.mapToDto(user.getLocation()) : null;

        return new UserResponseDto(
                user.getExternalId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail(),
                sex,
                user.getPhotoUrl(),
                locationDto
        );
    }
}