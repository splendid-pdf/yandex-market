package com.marketplace.sellerinfoservice.model;

import com.yandex.market.auth.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    private String email;

    private String password;

    private String legalAddress;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private BusinessModel businessModel;

    private String ITN;

    private String PSRN;

    private String BIC;

    private String paymentAccount;

    private String corporateAccount;

    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}