package com.market.userinfoservice.service;

import com.market.userinfoservice.model.User;
import com.market.userinfoservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "User wasn't found by id=";
    private final UserRepository userRepository;

    public User findUserByExternalId(UUID externalId) throws EntityNotFoundException {
        return userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + externalId));
    }
    @Transactional
    public void deleteUserByExternalId(UUID externalId) throws EntityNotFoundException {
        if(userRepository.findByExternalId(externalId).isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + externalId);
        }
        userRepository.deleteUserByExternalId(externalId);
    }



}
