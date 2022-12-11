package com.yandex.market.shopsystem.model.branch;

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
public class DeliveryInterval {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_interval_seq")
    @SequenceGenerator(name = "delivery_interval_seq", sequenceName = "delivery_interval_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    private String intervalId;

    private LocalTime periodStart;

    private LocalTime periodEnd;
}
