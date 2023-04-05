package com.marketplace.authorizationserver.service;

import com.marketplace.authorizationserver.dto.AuthClientDetails;
import com.yandex.market.auth.dto.ClientAuthDetails;
import com.marketplace.authorizationserver.gateway.SellerAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SellerDetailsService implements UserDetailsService {
    private final SellerAuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClientAuthDetails clientAuthDetails = authClient.receiveSellerAuthDetails(email);

        return new AuthClientDetails(
                clientAuthDetails.email(),
                clientAuthDetails.password(),
                Set.of(new SimpleGrantedAuthority("ROLE_".concat(clientAuthDetails.role()))),
                clientAuthDetails.uuid().toString()
        );
    }
}