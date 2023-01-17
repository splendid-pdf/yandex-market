package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.specification.UserSpecification;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.yandex.market.userinfoservice.utils.ValidationCodeConstants.NULL_LOCATION_CODE;
import static com.yandex.market.userinfoservice.utils.ValidationCodeConstants.NULL_NOTIFICATION_CODE;
@Service
@RequiredArgsConstructor
public class PrivateUserService {
    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
    private final UserResponseMapper userResponseMapper;
    private final ErrorInfoProperties properties;

    public List<UserResponseDto> getUsersByFilter(UserFilter userFilter) {
        filterValidate(userFilter);
        return userRepository.findAll(userSpecification.getSpecificationFromUserFilter(userFilter))
                .stream().map(userResponseMapper::map)
                .toList();
    }

    private void filterValidate(UserFilter userFilter) {
        if(Objects.isNull(userFilter.getLocation())){
            throw new ValidationException(properties.getMessageByErrorCode(NULL_LOCATION_CODE));
        }

        if(Objects.isNull(userFilter.getNotificationSettings())){
            throw new ValidationException(properties.getMessageByErrorCode(NULL_NOTIFICATION_CODE));
        }
    }
}
