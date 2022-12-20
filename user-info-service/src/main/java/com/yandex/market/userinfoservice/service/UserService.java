package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.converter.Converter;
import com.yandex.market.userinfoservice.dto.UserDto;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public static final String USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE =
            "User with similar email = %s is already exists";
    private final UserRepository userRepository;

    private final Converter converter;

    public UUID create(UserDto userDto) {
        User user = converter.convertUser(userDto);

        if (userRepository.existsByEmail(userDto.email())) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE.formatted(userDto.email()));
        }

        userRepository.save(user);
        return user.getExternalId();
    }

}
