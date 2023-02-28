package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.model.Location;
import org.openapitools.api.model.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location map(LocationDto locationDto) {
        return Location.builder()
                .country(locationDto.getCountry())
                .region(locationDto.getRegion())
                .city(locationDto.getCity())
                .postcode(locationDto.getPostcode())
                .street(locationDto.getStreet())
                .houseNumber(String.valueOf(locationDto.getHouseNumber()))
                .apartmentNumber(String.valueOf(locationDto.getApartNumber()))
                .build();
    }

    public LocationDto mapToDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setCountry(location.getCountry());
        locationDto.setRegion(location.getRegion());
        locationDto.setCity(location.getCity());
        locationDto.setPostcode(location.getPostcode());
        locationDto.setStreet(location.getStreet());
        locationDto.setHouseNumber(location.getHouseNumber());
        locationDto.setApartNumber(location.getApartmentNumber());
        return locationDto;
    }
}