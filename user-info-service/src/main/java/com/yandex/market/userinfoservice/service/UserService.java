package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.mapper.UserRequestMapper;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.specification.UserSpecification;
import com.yandex.market.userinfoservice.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public static final String USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE =
            "User with similar email = %s is already exists";
    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "User wasn't found by id =";
    private static final String USER_NOT_FOUND_MESSAGE_BY_VALUE = "User wasn't found by value =";
    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;

    private final UserResponseMapper userResponseMapper;

    private final UserValidator userValidator;

    @Transactional
    public UUID create(UserRequestDto userRequestDto) {
        userValidator.validate(userRequestDto);

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE.formatted(userRequestDto.getEmail()));
        }
        User user = userRequestMapper.map(userRequestDto);

        userRepository.save(user);
        return user.getExternalId();
    }

    public UserResponseDto findUserByExternalId(UUID externalId) throws EntityNotFoundException {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + externalId));
        return userResponseMapper.map(user);
    }

    @Transactional
    public void deleteUserByExternalId(UUID externalId) throws EntityNotFoundException {
        userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + externalId));
        userRepository.deleteUserByExternalId(externalId);
    }

    @Transactional
    public UserResponseDto update(UUID externalId, UserRequestDto userRequestDto) {
        User storedUser = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + externalId));
        User updatedUser = userRequestMapper.map(userRequestDto);
        storedUser.setEmail(updatedUser.getEmail());
        storedUser.setFirstName(updatedUser.getFirstName());
        storedUser.setMiddleName(updatedUser.getMiddleName());
        storedUser.setLastName(updatedUser.getLastName());
        storedUser.setBirthday(updatedUser.getBirthday());
        Stream.ofNullable(updatedUser.getContacts())
                .flatMap(Collection::stream)
                .forEach(storedUser::addContact);
        storedUser.setPhone(updatedUser.getPhone());
        storedUser.setSex(updatedUser.getSex());
        storedUser.setLocation(updatedUser.getLocation());
        storedUser.setLogin(updatedUser.getLogin());
        storedUser.setNotificationSettings(updatedUser.getNotificationSettings());
        storedUser.setPhotoId(updatedUser.getPhotoId());
        return userResponseMapper.map(storedUser);
    }

    public UserResponseDto getUserDtoByEmailOrPhone(String emailOrPhone) {
        //todo: regex

        if (emailOrPhone.contains("@")) {
            return userResponseMapper.map(userRepository.findUserByEmail(emailOrPhone)
                    .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_VALUE + emailOrPhone)));
        }

        return userResponseMapper.map(userRepository.findUserByPhone(emailOrPhone)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_VALUE + emailOrPhone)));
    }

    public List<UserResponseDto> getUsersByFilter (UserFilter userFilter) {
        return userRepository.findAll(UserSpecification.getSpecificationFromUserFilter(userFilter))
                .stream().map(userResponseMapper::map)
                .collect(Collectors.toList());
    }

}
