package com.market.userinfoservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "contacts")
@EqualsAndHashCode
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact-generator")
    @SequenceGenerator(name = "contact-generator")
    private Long id;

    private String value;

    private SocialContactType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;



}
