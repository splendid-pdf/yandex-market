package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.model.UserFavoritesPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserFavoritesPageRepository extends JpaRepository<UserFavoritesPage, Long> {

    Optional<UserFavoritesPage> findUserFavoritesPageByUserId(UUID userId);
}