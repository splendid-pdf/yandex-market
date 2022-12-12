package com.market.userinfoservice.service;

import com.market.userinfoservice.model.User;
import com.market.userinfoservice.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;

    public User findUserById(long id) throws EntityNotFoundException {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) {
            log.debug("GET public/api/v1/users/" + id + " EntityNotFound");
            throw new EntityNotFoundException("Entity \"User\" wasn't found id=" + id);
        }
        return userOptional.get();
    }

    public User deleteUserById(long id) throws EntityNotFoundException {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) {
            log.debug("DELETE public/api/v1/users/" + id + " EntityNotFound");
            throw new EntityNotFoundException("Entity \"User\" wasn't found id=" + id);
        }
        userRepo.deleteById(userOptional.get().getId());
        return userOptional.get();
    }



}
