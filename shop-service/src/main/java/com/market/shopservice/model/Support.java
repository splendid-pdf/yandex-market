package com.market.shopservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supports")
public class Support {

    @Id
    @Column(name = "support_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Support support = (Support) o;
        return Objects.equals(id, support.id) && Objects.equals(number, support.number)
                && Objects.equals(email, support.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, email);
    }
}
