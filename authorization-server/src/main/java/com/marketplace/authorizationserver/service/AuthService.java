package com.marketplace.authorizationserver.service;

import com.marketplace.authorizationserver.dto.UserAuthDto;
import com.marketplace.authorizationserver.model.СustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthDto userAuthDto = restTemplate.getForObject(
                "http://localhost:8080/private/api/v1/users/auth-details/" + email, UserAuthDto.class
        );
        return new СustomUserDetails(
                userAuthDto.email(),
                userAuthDto.encodedPassword(),
                Set.of(new SimpleGrantedAuthority(userAuthDto.role())),
                userAuthDto.uuid().toString()
        );
    }
}