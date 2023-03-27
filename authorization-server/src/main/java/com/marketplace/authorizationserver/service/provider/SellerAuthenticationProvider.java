package com.marketplace.authorizationserver.service.provider;

import com.marketplace.authorizationserver.dto.AuthClientDetails;
import com.marketplace.authorizationserver.service.CustomUserDetailsService;
import com.marketplace.authorizationserver.service.SellerDetailsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SellerAuthenticationProvider implements AuthenticationProvider {
    private final SellerDetailsService detailsService;

    private final AuthenticationManagerBuilder auth;

    @PostConstruct
    public void postConstruct() {
        auth.authenticationProvider(this);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        AuthClientDetails clientDetails = (AuthClientDetails) detailsService.loadUserByUsername(email);
        if (password.equals(clientDetails.getPassword())) {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(email, password, clientDetails.getAuthorities());

            token.setDetails(Map.of("seller-id", clientDetails.getExternalId()));
            return token;
        }
        throw new BadCredentialsException("Bad credentials for email: " + email);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
