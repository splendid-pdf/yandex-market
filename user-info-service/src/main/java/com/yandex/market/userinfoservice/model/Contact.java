package com.yandex.market.userinfoservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
@EqualsAndHashCode(of = "id")
public class Contact {

    @Id
    @SequenceGenerator(name = "contact-generator")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact-generator")
    private Long id;

    private String value;

    private SocialNetwork type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void setDefaultSocialNetwork(){
        type = SocialNetwork.NONE;
    }

}