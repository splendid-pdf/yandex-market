package com.yandex.market.userinfoservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contacts")
@EqualsAndHashCode(of = "id")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact-generator")
    @SequenceGenerator(name = "contact-generator")
    private Long id;

    private String value;

    private SocialNetwork type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}