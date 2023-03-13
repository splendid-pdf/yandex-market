package com.yandex.market.userservice.mapper;

import com.yandex.market.userservice.dto.response.LocationDto;
import com.yandex.market.userservice.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location map(LocationDto locationDto) {
        return Location.builder()
                .country(locationDto.country())
                .region(locationDto.region())
                .city(locationDto.city())
                .postcode(locationDto.postcode())
                .street(locationDto.street())
                .houseNumber(String.valueOf(locationDto.houseNumber()))
                .apartmentNumber(String.valueOf(locationDto.apartNumber()))
                .build();
    }

    public LocationDto mapToDto(Location location) {
        return new LocationDto(
                location.getCountry(),
                location.getCity(),
                location.getRegion(),
                location.getStreet(),
                location.getPostcode(),
                location.getHouseNumber(),
                location.getApartmentNumber(),
                location.getLatitude(),
                location.getLongitude()
        );
    }
}