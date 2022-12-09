package com.market.shopservice.models;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Support {
    private String phone;
    private String email;

    public Support(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }
}
