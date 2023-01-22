package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
    private final UserResponseMapper userResponseMapper;

    public List<UserResponseDto> getUsersByFilter(UserFilter userFilter) {
        return userRepository.findAll(userSpecification.getSpecificationFromUserFilter(userFilter))
                .stream().map(userResponseMapper::map)
                .toList();
    }
}