package com.marketplace.userservice.service;

import com.marketplace.userservice.dto.response.CreateUserResponse;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.validator.UserRegistrationValidator;
import com.marketplace.userservice.validator.UserValidator;
import com.yandex.market.auth.dto.ClientAuthDetails;
import com.marketplace.userservice.dto.request.UserRegistrationDto;
import com.marketplace.userservice.dto.request.UserRequestDto;
import com.marketplace.userservice.dto.response.UserResponseDto;
import com.marketplace.userservice.mapper.UserRequestMapper;
import com.marketplace.userservice.mapper.UserResponseMapper;
import com.yandex.market.auth.model.Role;
import com.marketplace.userservice.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.regex.Matcher;

import static com.marketplace.userservice.utils.ExceptionMessagesConstants.*;
import static com.marketplace.userservice.utils.PatternConstants.GROUPED_PHONE_NUMBERS_PATTERN;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final UserRegistrationValidator userRegistrationValidator;

    public CreateUserResponse signUp(UserRegistrationDto userDto) {
        userRegistrationValidator.validate(userDto);

        checkEmailForUniqueness(userDto.email());

        val user = new User()
                .externalId(UUID.randomUUID())
                .role(Role.USER)
                .email(userDto.email())
                .password(userDto.password());

        return new CreateUserResponse(userRepository.save(user).externalId());
    }

    @Cacheable(value = "users", key = "#externalId")
    public UserResponseDto findUserByExternalId(UUID externalId) {
        val user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        return userResponseMapper.map(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#externalId")
    public void deleteUserByExternalId(UUID externalId) {
        val user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        user.isDeleted(true);
    }

    @Transactional
    @CachePut(value = "users", key = "#externalId")
    public UserResponseDto update(UUID externalId, UserRequestDto userRequestDto) {
        userValidator.validate(userRequestDto);

        val storedUser = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));

        val updatedUser = userRequestMapper.map(userRequestDto);

        setPhoneIfChangedAndRemainedUnique(storedUser, updatedUser);
        setEmailIfChangedAndRemainedUnique(storedUser, updatedUser);

        storedUser
                .firstName(updatedUser.firstName())
                .lastName(updatedUser.lastName())
                .sex(updatedUser.sex())
                .photoId(updatedUser.photoId())
                .location(updatedUser.location());

        return userResponseMapper.map(storedUser);
    }

    public ClientAuthDetails getUserDetails(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE + email));
    }

    private void checkEmailForUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE.formatted(email));
        }
    }

    private String formatPhone(String phone) {
        StringBuilder formattedPhone = new StringBuilder(phone.replaceAll("\\D", ""));
        if (formattedPhone.length() == 10) {
            formattedPhone.insert(0, "7");
        }
        // todo если уже начинается с 7, то все равно сделает замену
        formattedPhone.replace(0, 1, "7");
        Matcher matcher = GROUPED_PHONE_NUMBERS_PATTERN.matcher(formattedPhone);
        if (matcher.find()) {
            formattedPhone = new StringBuilder();
            matcher.appendReplacement(formattedPhone, "+$1$2$3$4$5");
            matcher.appendTail(formattedPhone);
            return formattedPhone.toString();
        }
        return StringUtils.EMPTY;
    }

    private void checkPhoneForUniqueness(String formattedPhone) {
        if (userRepository.existsByPhone(formattedPhone)) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_PHONE_IS_EXISTS_MESSAGE.formatted(formattedPhone));
        }
    }

    private void setEmailIfChangedAndRemainedUnique(User storedUser, User updatedUser) {
        val email = updatedUser.email();
        if (ObjectUtils.notEqual(storedUser.email(), email)) {
            checkEmailForUniqueness(email);
            storedUser.email(email);
        }
    }

    private void setPhoneIfChangedAndRemainedUnique(User storedUser, User updatedUser) {
        val formattedPhone = formatPhone(updatedUser.phone());
        if (ObjectUtils.notEqual(storedUser.phone(), formattedPhone)) {
            checkPhoneForUniqueness(formattedPhone);
            storedUser.phone(formattedPhone);
        }
    }
}