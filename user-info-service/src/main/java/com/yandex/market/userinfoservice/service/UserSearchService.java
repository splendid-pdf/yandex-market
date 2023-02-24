package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.dto.UserAuthenticationDto;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.specification.UserSpecification;
import com.yandex.market.userinfoservice.validator.UserFilterValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yandex.market.userinfoservice.utils.ExceptionMessagesConstants.USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
    private final UserResponseMapper userResponseMapper;

    private final UserFilterValidator userFilterValidator;

    public List<UserResponseDto> getUsersByFilter(UserFilter userFilter) {
        userFilterValidator.validate(userFilter);

        return userRepository.findAll(userSpecification.getSpecificationFromUserFilter(userFilter))
                .stream().map(userResponseMapper::map)
                .toList();
    }

    public UserAuthenticationDto authenticate(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE + email));
        return new UserAuthenticationDto(
                user.getExternalId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name()
        );
    }
}