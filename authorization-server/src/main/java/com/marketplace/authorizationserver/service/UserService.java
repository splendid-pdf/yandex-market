package com.marketplace.authorizationserver.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("user", "$2y$10$brCYckFZmLUXfjX46WOrpudAdlaSfV5iskbdD7BdQj8cW.wjZdB3i", Stream.generate(
                () -> new SimpleGrantedAuthority("USER"))
                .limit(1)
                .collect(Collectors.toSet()));
    }
}
