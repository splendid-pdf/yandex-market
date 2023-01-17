package com.yandex.market.shopservice.model.branch;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_intervals")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeliveryInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_interval_seq")
    @SequenceGenerator(name = "delivery_interval_seq", sequenceName = "delivery_interval_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    @EqualsAndHashCode.Include
    private String intervalId;

    private LocalTime periodStart;

    private LocalTime periodEnd;
}
