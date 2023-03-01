package com.yandex.market.sellerinfoservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sellers")
@EqualsAndHashCode(of = "id")
public class Seller {

    @Id
    @SequenceGenerator(name = "sellers_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sellers_sequence")
    private Long id;

    private UUID externalId;

    private String firstName;

    private String lastName;

    private String legalAddress;

    private String companyName;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private BusinessModel businessModel;

    //ITN - Идентификационный номер налогоплательщика
    private String ITN;

    //PSRN - Основной государственный регистрационный номер
    private String PSRN;

    // BIC - Банковский идентификационный код
    private String BIC;

    private String paymentAccount;

    private String corporateAccount;
}