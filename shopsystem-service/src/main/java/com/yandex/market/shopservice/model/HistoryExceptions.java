package com.yandex.market.shopservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "history_exceptions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoryExceptions {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_exception_seq")
    @SequenceGenerator(name = "history_exception_seq", sequenceName = "history_exception_sequence", allocationSize = 1)
    private Long id;
    private String reason;
    private String message;
    @CreationTimestamp
    private LocalDateTime dateTime;

    public HistoryExceptions(String reason, String message) {
        this.reason = reason;
        this.message = message;
    }
}
