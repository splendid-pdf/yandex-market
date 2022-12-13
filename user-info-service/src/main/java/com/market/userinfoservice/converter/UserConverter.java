package com.market.userinfoservice.converter;

import com.market.userinfoservice.dto.UserDto;
import com.market.userinfoservice.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserConverter {
    public User convert(UserDto userDto) {
        return User.builder()
                .externalId(UUID.randomUUID())
                .firstName(userDto.firstName())
                .middleName(userDto.middleName())
                .lastName(userDto.lastName())
                .phone(userDto.phone())
                .email(userDto.email())
                .login(userDto.login())
                .password(userDto.password())
                .birthday(userDto.birthday())
                .sex(userDto.sex())
                .build();
    }
}
