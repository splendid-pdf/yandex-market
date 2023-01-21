package com.yandex.market.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-sequence")
    @SequenceGenerator(name = "order-sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private UUID externalId;

    private UUID userId;

    private double price;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    //@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
    @OneToMany( fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderedProduct> orderedProducts;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Embedded
    private ReceiptMethod receiptMethod;

    private boolean paid;

    private LocalDateTime paymentDateTime;

    private LocalDateTime creationTimestamp;

    @Override
    public String toString() {
        return "Order{" +
                "userId=" + userId +
                ", price=" + price +
                ", orderStatus=" + orderStatus +
                ", orderedProducts=" + orderedProducts +
                ", paymentType=" + paymentType +
                ", receiptMethod=" + receiptMethod +
                ", paid=" + paid +
                ", paymentDateTime=" + paymentDateTime +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}