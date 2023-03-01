package com.yandex.market.sellerinfoservice.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Seller {

    @Id
    private Long id;

    private UUID externalId;

    private String name;

    private String surName;

    private String legalAddress;

    private String companyName;

    private String imageUrl;

    @Embedded
    private BusinessModel businessModel;

    private String individualTaxpayerNumber;

    private String primaryStateRegistrationNumber;

    private String BIC;

    private String paymentAccount;

    private String corporateAccount;
}