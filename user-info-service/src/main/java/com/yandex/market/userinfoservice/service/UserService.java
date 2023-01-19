package com.yandex.market.userinfoservice.service;

import com.yandex.market.userinfoservice.mapper.UserRequestMapper;
import com.yandex.market.userinfoservice.mapper.UserResponseMapper;
import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import com.yandex.market.userinfoservice.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.yandex.market.userinfoservice.utils.ExceptionMessagesConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final UserValidator userValidator;

    @Transactional
    public UUID create(UserRequestDto userRequestDto) {
        userValidator.validate(userRequestDto);

        String formattedPhone = checkPhoneForUniqueness(userRequestDto);
        checkEmailForUniqueness(userRequestDto);

        //todo: must pay attention!!
        User user = userRequestMapper.map(userRequestDto);
        user.setPhone(formattedPhone);
        userRepository.save(user);
        return user.getExternalId();
    }

    private void checkEmailForUniqueness(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE
                    .formatted(userRequestDto.getEmail()));
        }
    }

    private String checkPhoneForUniqueness(UserRequestDto userRequestDto) {
        String formattedPhone = formatPhone(userRequestDto.getPhone());
        if (userRepository.existsByPhone(formattedPhone)) {
            throw new IllegalArgumentException(USER_WITH_THE_SAME_PHONE_IS_EXISTS_MESSAGE
                    .formatted(userRequestDto.getPhone()));
        }
        return formattedPhone;
    }

    private String formatPhone(String phone) {
        StringBuilder formattedPhone = new StringBuilder(phone.replaceAll("\\D", ""));
        if (formattedPhone.length() == 10) {
            formattedPhone.insert(0, "7");
        }
        formattedPhone.replace(0, 1, "7");
        Pattern pattern = Pattern.compile("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})");
        Matcher matcher = pattern.matcher(formattedPhone);
        if (matcher.find()) {
            formattedPhone = new StringBuilder();
            matcher.appendReplacement(formattedPhone, "+$1($2)$3-$4-$5");
            matcher.appendTail(formattedPhone);
            return formattedPhone.toString();
        }
        return StringUtils.EMPTY;
    }

    @Cacheable(value = "users", key = "#externalId")
    public UserResponseDto findUserByExternalId(UUID externalId) {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + externalId));
        return userResponseMapper.map(user);
    }

    @CacheEvict(value = "users", key = "#externalId")
    @Transactional
    public void deleteUserByExternalId(UUID externalId){
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + externalId));
        user.setDeleted(true);
    }

    @CachePut(value = "users", key = "#externalId")
    @Transactional
    public UserResponseDto update(UUID externalId, UserRequestDto userRequestDto) {

        userValidator.validate(userRequestDto);

        User storedUser = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + externalId));
        User updatedUser = userRequestMapper.map(userRequestDto);

        //todo: delete unique constraint on email field

        if(!storedUser.getEmail().equals(updatedUser.getEmail())) {
            checkEmailForUniqueness(userRequestDto);
            storedUser.setEmail(updatedUser.getEmail());
        }

        if(!storedUser.getPhone().equals(formatPhone(updatedUser.getPhone()))) {
            checkPhoneForUniqueness(userRequestDto);
            storedUser.setPhone(formatPhone(updatedUser.getPhone()));
        }

        //to method
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
        return userResponseMapper.map(storedUser);
    }

    //TODO: performance trouble, big big trouble!!!!!

    @Cacheable(value = "users", key = "#emailOrPhone")
    public UserResponseDto getUserDtoByEmailOrPhone(String emailOrPhone) {
        //todo: regex

        if (emailOrPhone.contains("@")) {
            return userResponseMapper.map(userRepository.findUserByEmail(emailOrPhone)
                    .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_VALUE + emailOrPhone)));
        }

        return userResponseMapper.map(userRepository.findUserByPhone(emailOrPhone)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_VALUE + emailOrPhone)));
    }

}
