package com.yandex.market.userservice.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yandex.market.userservice.config.properties.RestProperties;
import com.yandex.market.userservice.model.Location;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TwoGisClient {

    private static final String QUERY_TEMPLATE = "%s, %s, %s";

    private final RestTemplate restTemplate;
    private final RestProperties properties;

    public Optional<Point> findCoordinatesByLocation(Location location) {
        if (StringUtils.isNoneBlank(location.getCity(), location.getStreet(), location.getHouseNumber())) {
            String addressQuery = QUERY_TEMPLATE.formatted(
                    location.getCity(), location.getStreet(), location.getHouseNumber());

            return Optional.ofNullable(restTemplate.getForObject(
                                    properties.geocoderUrl(),
                                    PlaceDetails.class,
                                    addressQuery,
                                    properties.geocoderApiKey()))
                    .map(PlaceDetails::result)
                    .map(SearchResult::places)
                    .map(list -> list.get(0))
                    .map(PlaceInfo::point);
        }

        return Optional.empty();
    }

    public record PlaceDetails(Meta meta, SearchResult result) {

    }

    public record Meta(String apiVersion, int code, String issueDate) {
    }

    public record SearchResult(int total, @JsonProperty("items") List<PlaceInfo> places) {
    }

    public record PlaceInfo(String id,
                            String fullName,
                            String addressName,
                            String name,
                            Point point,
                            String purposeName,
                            String type) {
    }

    public record Point(double lat, double lon) {
    }
}