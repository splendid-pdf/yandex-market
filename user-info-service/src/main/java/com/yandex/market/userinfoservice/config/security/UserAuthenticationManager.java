package com.yandex.market.userinfoservice.config.security;

import com.yandex.market.userinfoservice.model.User;
import com.yandex.market.userinfoservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.yandex.market.userinfoservice.utils.ExceptionMessagesConstants.USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE;
import static com.yandex.market.userinfoservice.utils.ExceptionMessagesConstants.USER_NOT_FOUND_BY_PHONE_ERROR_MESSAGE;
import static com.yandex.market.userinfoservice.utils.PatternConstants.PHONE_PATTERN;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationManager {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    public boolean externalIdMatches(Authentication authentication, UUID externalId) {
        String authenticationUserExternalId = getAuthenticationUserExternalId(authentication);
        return externalId.toString().equals(authenticationUserExternalId);
    }

    public boolean externalIdMatchesByEmailOrPhone(Authentication authentication, String emailOrPhone) {
        if (PHONE_PATTERN.matcher(emailOrPhone).matches()) {
            return externalIdMatchesByPhone(authentication, emailOrPhone);
        } else if (EmailValidator.getInstance().isValid(emailOrPhone)) {
            return externalIdMatchesByEmail(authentication, emailOrPhone);
        }
        return false;
    }
    private boolean externalIdMatchesByEmail(Authentication authentication, String email) {
        String authenticationUserExternalId = getAuthenticationUserExternalId(authentication);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE + email));
        return user.getExternalId().toString().equals(authenticationUserExternalId);
    }

    private boolean externalIdMatchesByPhone(Authentication authentication, String phone) {
        String authenticationUserExternalId = getAuthenticationUserExternalId(authentication);
        User user = userRepository.findUserByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_PHONE_ERROR_MESSAGE + phone));
        return user.getExternalId().toString().equals(authenticationUserExternalId);
    }

    private String getAuthenticationUserExternalId(Authentication authentication) {
        return restTemplate.getForObject(
                "http://127.0.0.1:9000/authentication/" + authentication.getName(),
                String.class
        );
    }
}