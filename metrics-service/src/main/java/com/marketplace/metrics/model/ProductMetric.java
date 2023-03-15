package com.marketplace.metrics.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "metrics")
@EqualsAndHashCode(of = "id")
public class ProductMetric {

    @Id
    @SequenceGenerator(name = "metrics_generator", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metrics_generator")
    private Long id;

    private String userAction;

    private String userId;

    private String productExternalId;

    private String productName;

    private LocalDateTime timestamp;
}
