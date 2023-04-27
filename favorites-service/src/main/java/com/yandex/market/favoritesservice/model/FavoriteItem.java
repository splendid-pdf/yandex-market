package com.yandex.market.favoritesservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@Table(name = "favorites")
@EqualsAndHashCode(of = "id")
public class FavoriteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorites_sequence")
    @SequenceGenerator(name = "favorites_sequence", allocationSize = 1)
    private Long id;

    @Builder.Default
    @Column(unique = true)
    private UUID externalId = UUID.randomUUID();

    private UUID userId;

    @Builder.Default
    @OneToMany(mappedBy = "favoriteItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteProduct> products = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "favoriteItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteSeller> sellers = new ArrayList<>();

    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();

    public FavoriteItem addProduct(FavoriteProduct product) {
        product.setFavoriteItem(this);
        products.add(product);
        return this;
    }

    public void removeProduct(FavoriteProduct product) {
        products.remove(product);
    }

    public FavoriteItem addSeller(FavoriteSeller seller) {
        seller.setFavoriteItem(this);
        sellers.add(seller);
        return this;
    }

    public void removeSeller(FavoriteSeller seller) {
        sellers.remove(seller);
    }
}