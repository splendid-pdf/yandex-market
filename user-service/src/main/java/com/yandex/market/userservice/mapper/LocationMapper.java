package com.yandex.market.userservice.mapper;

import com.yandex.market.userservice.dto.response.LocationDto;
import com.yandex.market.userservice.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location map(LocationDto locationDto) {
        return Location.builder()
                .city(locationDto.city())
                .deliveryAddress(locationDto.deliveryAddress())
                .build();
    }

    public LocationDto mapToDto(Location location) {
        return new LocationDto(
                location.getCity(),
                location.getDeliveryAddress()
        );
    }
}