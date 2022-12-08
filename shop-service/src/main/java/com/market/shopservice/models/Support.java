package com.market.shopservice.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Support {
    private String phone;
    private String email;
}
