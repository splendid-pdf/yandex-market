package com.yandex.market.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-sequence")
    @SequenceGenerator(name = "order-sequence", allocationSize = 1)
    private Long id;

    private UUID externalId;

    private UUID userId;

    @Embedded
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductDetails> productDetails;

    @Embedded
    private PaymentMethod paymentMethod;

    @Embedded
    private ReceiptMethod receiptMethod;

    private boolean paid;

    private LocalDateTime paidTimestamp;

    private LocalDateTime creationTimestamp;
}