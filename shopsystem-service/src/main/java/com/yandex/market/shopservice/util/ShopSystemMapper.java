package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.dto.SupportDto;
import com.yandex.market.shopservice.model.shop.Location;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.model.shop.Support;

import java.util.List;
import java.util.stream.Collectors;

public class ShopSystemMapper {
    public ShopSystem toShopSystemFromDto(ShopSystemDto dto) {
        return ShopSystem.builder()
                .name(dto.getName())
                .token(dto.getToken())
                .support(toSupportFromDto(dto.getSupport()))
                .legalEntityAddress(toLocationFromDto(dto.getLegalEntityAddress()))
                .specialOffers(dto.getSpecialOffers())
                .branches(dto.getBranches())
                .logoUrl(dto.getLogoUrl())
                .rating(dto.getRating())
                .build();
    }

    public ShopSystemDto toShopSystemDto(ShopSystem shopSystem) {
        return ShopSystemDto.builder()
                .name(shopSystem.getName())
                .token(shopSystem.getToken())
                .support(toSupportDto(shopSystem.getSupport()))
                .legalEntityAddress(toLocationDto(shopSystem.getLegalEntityAddress()))
                .specialOffers(shopSystem.getSpecialOffers())
                .branches(shopSystem.getBranches())
                .logoUrl(shopSystem.getLogoUrl())
                .rating(shopSystem.getRating())
                .build();
    }

    public SupportDto toSupportDto(Support support) {
        return new SupportDto(support.getNumber(), support.getEmail());
    }

    public Support toSupportFromDto(SupportDto dto) {
        return Support.builder()
                .number(dto.number())
                .email(dto.email())
                .build();
    }

    public Location toLocationFromDto(LocationDto dto) {
        return Location.builder()
                .country(dto.country())
                .region(dto.region())
                .city(dto.city())
                .street(dto.street())
                .houseNumber(dto.houseNumber())
                .officeNumber(dto.officeNumber())
                .postcode(dto.postcode())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .build();
    }

    public LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getCountry(),
                location.getRegion(),
                location.getCity(),
                location.getStreet(),
                location.getHouseNumber(),
                location.getOfficeNumber(),
                location.getPostcode(),
                location.getLatitude(),
                location.getLongitude()
        );
    }

    public List<ShopSystemDto> toShopSystemDtoPages(List<ShopSystem> content) {
        return content.stream()
                .map(this::toShopSystemDto)
                .collect(Collectors.toList());
    }
}
