package com.market.userinfoservice.converter;

import com.market.userinfoservice.dto.UserDto;
import com.market.userinfoservice.model.Sex;
import com.market.userinfoservice.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class Converter {
    public User convertUser(UserDto userDto) {
        return User.builder()
                .externalId(UUID.randomUUID())
                .firstName(userDto.firstName())
                .middleName(userDto.middleName())
                .lastName(userDto.lastName())
                .phone(userDto.phone())
                .email(userDto.email())
                .login(userDto.login())
                .password(userDto.password())
                .birthday(convertLocalDate(userDto.birthday()))
                .sex(convertSex(userDto.sex()))
                .build();
    }

    public LocalDate convertLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    public Sex convertSex(String sex){
        return Sex.valueOf(sex);
    }
}
