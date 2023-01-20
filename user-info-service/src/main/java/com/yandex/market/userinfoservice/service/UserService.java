package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.gateway.TwoGisClient;
import com.yandex.market.userinfoservice.mapper.UserRequestMapper;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.model.Location;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static com.yandex.market.userinfoservice.utils.ExceptionMessagesConstants.*;
import static com.yandex.market.userinfoservice.utils.PatternConstants.GROUPED_PHONE_NUMBERS_PATTERN;
import static com.yandex.market.userinfoservice.utils.PatternConstants.PHONE_PATTERN;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final TwoGisClient client;
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    @Transactional
    public UUID create(UserRequestDto userRequestDto) {
        userValidator.validate(userRequestDto);

        checkEmailForUniqueness(userRequestDto.getEmail());

        val formattedPhone = formatPhone(userRequestDto.getPhone());
        checkPhoneForUniqueness(formattedPhone);

        val user = userRequestMapper.map(userRequestDto);
        user.setPhone(formattedPhone);

        val location = user.getLocation();
        client.findCoordinatesByLocation(location)
                .ifPresent(point -> setLocationCoordinates(location, point));

        return userRepository.save(user).getExternalId();
    }

    @Cacheable(value = "users", key = "#externalId")
    public UserResponseDto findUserByExternalId(UUID externalId) {
        val user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        return userResponseMapper.map(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#externalId")
    public void deleteUserByExternalId(UUID externalId){
        val user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        user.setDeleted(true);
    }

    @Transactional
    @CachePut(value = "users", key = "#externalId")
    public UserResponseDto update(UUID externalId, UserRequestDto userRequestDto) {
        userValidator.validate(userRequestDto);

        val storedUser = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));

        val updatedUser = userRequestMapper.map(userRequestDto);

        setEmailIfChangedAndRemainedUnique(storedUser, updatedUser);
        setPhoneIfChangedAndRemainedUnique(storedUser, updatedUser);

        updateUser(storedUser, updatedUser);

        return userResponseMapper.map(storedUser);
    }

    @Cacheable(value = "users", key = "#emailOrPhone")
    public UserResponseDto getUserDtoByEmailOrPhone(String emailOrPhone) {
        if (PHONE_PATTERN.matcher(emailOrPhone).matches()) {
            val phone = formatPhone(emailOrPhone);
            return userResponseMapper.map(getUserByPhone(phone));
        } else if (EmailValidator.getInstance().isValid(emailOrPhone)) {
            return userResponseMapper.map(getUserByEmail(emailOrPhone));
        }

        log.error("Given email or phone is not valid: emailOrPhone - " + emailOrPhone);
        throw new IllegalArgumentException("Invalid input data - " + emailOrPhone);
    }

    private void checkEmailForUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE
                    .formatted(email));
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
            matcher.appendReplacement(formattedPhone, "+$1($2)$3-$4-$5");
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

    private static void setLocationCoordinates(Location location, TwoGisClient.Point point) {
        location.setLatitude(point.lat());
        location.setLongitude(point.lon());
    }

    private void updateUser(User storedUser, User updatedUser) {
        storedUser.setFirstName(updatedUser.getFirstName());
        storedUser.setMiddleName(updatedUser.getMiddleName());
        storedUser.setLastName(updatedUser.getLastName());
        storedUser.setBirthday(updatedUser.getBirthday());
        Stream.ofNullable(updatedUser.getContacts())
                .flatMap(Collection::stream)
                .forEach(storedUser::addContact);
        storedUser.setSex(updatedUser.getSex());
        storedUser.setLocation(updatedUser.getLocation());
        storedUser.setLogin(updatedUser.getLogin());
        storedUser.setNotificationSettings(updatedUser.getNotificationSettings());
        storedUser.setPhotoId(updatedUser.getPhotoId());
    }

    private void setEmailIfChangedAndRemainedUnique(User storedUser, User updatedUser) {
        val email = updatedUser.getEmail();
        if (ObjectUtils.notEqual(storedUser.getEmail(), email)) {
            checkEmailForUniqueness(email);
            storedUser.setEmail(email);
        }
    }

    private void setPhoneIfChangedAndRemainedUnique(User storedUser, User updatedUser) {
        val formattedPhone = formatPhone(updatedUser.getPhone());
        if (ObjectUtils.notEqual(storedUser.getPhone(), formattedPhone)) {
            checkPhoneForUniqueness(formattedPhone);
            storedUser.setPhone(formattedPhone);
        }
    }

    private User getUserByPhone(String emailOrPhone) {
        return userRepository.findUserByPhone(emailOrPhone)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_PHONE_ERROR_MESSAGE + emailOrPhone));
    }

    private User getUserByEmail(String emailOrPhone) {
        return userRepository.findUserByEmail(emailOrPhone)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE + emailOrPhone));
    }
}