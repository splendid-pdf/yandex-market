package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.FavoritesRequestDto;
import com.yandex.market.favoritesservice.dto.FavoritesResponseDto;
import com.yandex.market.favoritesservice.mapper.FavoritesMapper;
import com.yandex.market.favoritesservice.model.Favorites;
import com.yandex.market.favoritesservice.repository.FavoritesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesMapper mapper;

    private final FavoritesRepository repository;

    @Override
    @Transactional
    public UUID createFavorites(FavoritesRequestDto favoritesRequestDto, UUID userId) {
        Favorites favorites = mapper.toFavorites(favoritesRequestDto);
        favorites.setUserId(userId);
        return repository.save(favorites).getExternalId();
    }

    @Override
    @Transactional
    public Page<FavoritesResponseDto> getFavoritesByUserId(UUID userId, Pageable page) {
        Page<Favorites> pageFavorites = repository.getFavoritesByUserId(userId, page);
        return new PageImpl<>(pageFavorites.getContent().stream()
                .map(mapper::toFavoritesResponseDto)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteFavoritesByUserId(UUID userId, UUID productId) {
        repository.deleteByUserIdAndProductId(userId, productId);
    }
}