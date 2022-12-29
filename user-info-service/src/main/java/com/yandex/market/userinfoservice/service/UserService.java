package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.mapper.UserMapper;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.validate.Validator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User wasn't found by id =";

    private static final String USER_NOT_FOUND_MESSAGE_EMAIL_OR_PHONE = "User wasn't found by this value =";

    public static final String USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE =
            "User with similar email = %s is already exists";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final Validator validator;

    @Transactional
    public UUID create(UserRequestDto userRequestDto) {
        validator.validateDto(userRequestDto);
        //todo Валидация
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE.formatted(userRequestDto.getEmail()));
        }
        User user = userMapper.map(userRequestDto);

        userRepository.save(user);
        return user.getExternalId();
    }

    public UserResponseDto findUserByExternalId(UUID externalId) throws EntityNotFoundException {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + externalId));
        return userMapper.mapToResponseDto(user);
    }

    @Transactional
    public UserResponseDto deleteUserByExternalId(UUID externalId) throws EntityNotFoundException {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + externalId));
        userRepository.deleteUserByExternalId(externalId);
        return userMapper.mapToResponseDto(user);
    }

    @Transactional
    public UserResponseDto update(UUID externalId, UserRequestDto userRequestDto) {
        User storedUser = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + externalId));
        User updatedUser = userMapper.map(userRequestDto);
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
        return userMapper.mapToResponseDto(storedUser);
    }

    public UserResponseDto getByEmailOrPhone(String emailOrPhone) {
        return userMapper.mapToResponseDto(userRepository.findUserByEmailOrPhone(emailOrPhone)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_EMAIL_OR_PHONE + emailOrPhone)));
    }

}
