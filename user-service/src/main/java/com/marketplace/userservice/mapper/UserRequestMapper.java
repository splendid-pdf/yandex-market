package com.marketplace.userservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.marketplace.userservice.dto.request.UserRequestDto;
import com.marketplace.userservice.model.Sex;
import com.marketplace.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRequestMapper implements Mapper<UserRequestDto, User> {
    private final LocationMapper locationMapper;

    @Override
    public User map(UserRequestDto userRequestDto) {
        return new User()
                .externalId(UUID.randomUUID())
                .firstName(userRequestDto.firstName())
                .lastName(userRequestDto.lastName())
                .phone(userRequestDto.phone())
                .email(userRequestDto.email())
                .login(trimEmailForLogin(userRequestDto))
                .sex(getSex(userRequestDto.sex()))
                .location(locationMapper.map(userRequestDto.location()))
                .photoId(userRequestDto.photoId());
    }

    private String trimEmailForLogin(UserRequestDto userRequestDto) {
        return userRequestDto.email().replaceAll("@[a-zA-Z0-9.-]+$", "");
    }

    private Sex getSex(String sex) {
        return StringUtils.isNotEmpty(sex) ? Sex.valueOf(sex) : Sex.NONE;
    }
}