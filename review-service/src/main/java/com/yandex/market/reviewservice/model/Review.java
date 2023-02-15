package com.yandex.market.reviewservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
@EqualsAndHashCode(of = "id")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review-sequence")
    @SequenceGenerator(name = "review-sequence", allocationSize = 1)
    private Long id;

    private UUID externalId;

    private UUID userId;

    private UUID productId;

    @Enumerated(value = EnumType.STRING)
    private ReviewType reviewType;

    private String advantages;

    private String disadvantages;

    private String commentary;

    private int score;

    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "review_photo_ids", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "photo_id")
    private List<String> photoIds = new ArrayList<>();
}