package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.dto.UserDto;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public User map(UserDto userDto) {
        return User.builder()
                .externalId(UUID.randomUUID())
                .firstName(userDto.firstName())
                .middleName(userDto.middleName())
                .lastName(userDto.lastName())
                .phone(userDto.phone())
                .email(userDto.email())
                .login(userDto.login())
                .password(userDto.password())
                .birthday(LocalDate.parse(userDto.birthday(), DateTimeFormatter.ISO_DATE))
                .sex(Sex.valueOf(userDto.sex()))
                .build();
    }
}