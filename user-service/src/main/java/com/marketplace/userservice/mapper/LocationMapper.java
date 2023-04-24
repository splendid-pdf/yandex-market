package com.marketplace.userservice.mapper;

import com.marketplace.userservice.dto.response.LocationDto;
import com.marketplace.userservice.model.Location;
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