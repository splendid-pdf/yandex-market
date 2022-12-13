package com.market.userinfoservice.service;

import com.market.userinfoservice.converter.UserConverter;
import com.market.userinfoservice.dto.UserDto;
import com.market.userinfoservice.model.User;
import com.market.userinfoservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    public void create(UserDto userDto) {
        User user = userConverter.convert(userDto);
        userRepository.save(user);
    }


}
