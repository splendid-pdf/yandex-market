package com.marketplace.authorizationserver.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthClientDetails extends User {

    private final String externalId;

    public AuthClientDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            String externalId
    ) {
        super(username, password, authorities);
        this.externalId = externalId;
    }

    public String getExternalId() {
        return externalId;
    }
}