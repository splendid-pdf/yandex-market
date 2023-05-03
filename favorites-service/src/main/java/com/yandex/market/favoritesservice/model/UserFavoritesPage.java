package com.yandex.market.favoritesservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites")
@EqualsAndHashCode(of = "id")
public class UserFavoritesPage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorites_seq")
    @SequenceGenerator(name = "favorites_seq", allocationSize = 1)
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

    public void addProduct(FavoriteProduct product) {
        product.setUserFavoritesPage(this);
        products.add(product);
    }

    public void addSeller(FavoriteSeller seller) {
        seller.setUserFavoritesPage(this);
        sellers.add(seller);
    }
}