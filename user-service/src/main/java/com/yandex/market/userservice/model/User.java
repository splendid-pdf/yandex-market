package com.yandex.market.userservice.model;

import com.yandex.market.auth.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @SequenceGenerator(name = "user-sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user-sequence")
    private Long id;

    private UUID externalId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String phone;

    private String email;

    private String login;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private LocalDate birthday;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    @Embedded
    private Location location;

    @Embedded
    private NotificationSettings notificationSettings;

    private String photoUrl;

    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}