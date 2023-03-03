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

    private String legalAddress;

    private String companyName;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private BusinessModel businessModel;

    private String ITN;

    private String PSRN;

    private String BIC;

    private String paymentAccount;

    private String corporateAccount;
}