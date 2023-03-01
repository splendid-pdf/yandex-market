package com.marketplace.authorizationserver.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUserDetails extends User {

    private final String externalId;

    public AuthUserDetails(
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