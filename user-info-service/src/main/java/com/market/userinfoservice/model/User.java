package com.market.userinfoservice.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@EqualsAndHashCode
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user-generator")
    @SequenceGenerator(name = "user-generator", allocationSize = 1)
    private Long id;

    private UUID externalId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String phone;

    private String email;

    private String login;

    private String password;

    private LocalDate birthday;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Contact> contacts = new HashSet<>();

    @Embedded
    private NotificationSettings notificationSettings;

    private String photoId;

    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public void addContact(Contact contact) {
        contact.setUser(this);
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

}
