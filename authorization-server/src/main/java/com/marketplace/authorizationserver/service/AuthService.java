package com.marketplace.authorizationserver.service;

import com.marketplace.authorizationserver.config.properties.SecurityProperties;
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

    private static final String ROLE_PREFIX = "ROLE_";

    private final SecurityProperties securityProperties;

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthDto userAuthDto = getUserAuthDto(email);

        return new СustomUserDetails(
                userAuthDto.email(),
                userAuthDto.password(),
                Set.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(userAuthDto.role()))),
                userAuthDto.uuid().toString()
        );
    }

    public String getExternalId(String email) {
        UserAuthDto userAuthDto = getUserAuthDto(email);

        СustomUserDetails userDetails = new СustomUserDetails(
                userAuthDto.email(),
                userAuthDto.password(),
                Set.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(userAuthDto.role()))),
                userAuthDto.uuid().toString());

        return userDetails.getExternalId();
    }

    private UserAuthDto getUserAuthDto(String email) {
        UserAuthDto userAuthDto = restTemplate.getForObject(
                securityProperties.getDataProviderUri() + email,
                UserAuthDto.class
        );

        if (userAuthDto == null) {
            throw new UsernameNotFoundException("User not found by email: " + email);
        }

        return userAuthDto;
    }
}