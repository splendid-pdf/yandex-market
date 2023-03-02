package com.yandex.market.sellerinfoservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Модель для \"Продавца\"")
public class Seller {

    @Id
    @SequenceGenerator(name = "sellers_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sellers_sequence")
    private Long id;

    private UUID externalId;

    private String firstName;

    private String lastName;

    private String email;

    @Schema(description = "Адрес продавца")
    private String legalAddress;

    private String companyName;

    private String imageUrl;

    @Schema(description = "Форма регистрации фирмы продавца")
    @Enumerated(value = EnumType.STRING)
    private BusinessModel businessModel;

    @Schema(description = "Идентификационный номер налогоплательщика продавца")
    private String ITN;

    @Schema(description = "Основной государственный регистрационный номер продавца")
    private String PSRN;

    @Schema(description = "Банковский идентификационный код продавца")
    private String BIC;

    @Schema(description = "Банковский расчетный счет продавца")
    private String paymentAccount;

    @Schema(description = "Банковский корпаративный счет продавца")
    private String corporateAccount;
}