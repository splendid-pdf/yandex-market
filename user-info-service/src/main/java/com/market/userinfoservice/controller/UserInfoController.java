package com.market.userinfoservice.controller;

import com.market.userinfoservice.model.User;
import com.market.userinfoservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserData(@PathVariable("id") long id) throws EntityNotFoundException {
        User user = userService.findUserById(id);
        log.info("GET public/api/v1/users/" + id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) throws EntityNotFoundException {
        User user = userService.deleteUserById(id);
        log.info("DELETE public/api/v1/users/" + id);
        return ResponseEntity.ok().body(user);
    }
}
