package com.yandex.market.userinfoservice.specification;

import com.yandex.market.userinfoservice.model.Sex;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserFilter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class FilterOrganizer {
    void buildFirstNameFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        val firstName = userFilter.getFirstName();
        if (!StringUtils.isBlank(firstName)) {
            filters.add(Filter.builder()
                    .aClass(String.class)
                    .field("firstName")
                    .operator(QueryOperations.LIKE)
                    .value(firstName)
                    .build());
        }
    }

    void buildLastNameFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        val lastName = userFilter.getLastName();
        if (!StringUtils.isBlank(lastName)) {
            filters.add(Filter.builder()
                    .aClass(String.class)
                    .field("lastName")
                    .operator(QueryOperations.LIKE)
                    .value(lastName)
                    .build());
        }
    }

    void buildSexFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        val sex = userFilter.getSex();
        if (StringUtils.isNotBlank(sex)) {
            filters.add(Filter.builder()
                    .aClass(Sex.class)
                    .field("sex")
                    .operator(QueryOperations.EQUALS)
                    .value(Sex.valueOf(sex.toUpperCase()))
                    .build());
        }
    }

    void buildBirthdayFromFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        val birthdayFrom = userFilter.getBirthdayFrom();
        if (birthdayFrom != null) {
            filters.add(Filter.builder()
                    .aClass(LocalDate.class)
                    .field("birthday")
                    .operator(QueryOperations.GREATER_THAN)
                    .value(birthdayFrom)
                    .build());
        }
    }

    void buildBirthdayToFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        val birthdayTo = userFilter.getBirthdayTo();
        if (birthdayTo != null) {
            filters.add(Filter.builder()
                    .aClass(LocalDate.class)
                    .field("birthday")
                    .operator(QueryOperations.LESS_THAN)
                    .value(birthdayTo)
                    .build());
        }
    }

    void buildCityFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getLocation())) {
            val city = userFilter.getLocation().getCity();
            if (!StringUtils.isBlank(city)) {
                filters.add(Filter.builder()
                        .aClass(String.class)
                        .field("city")
                        .operator(QueryOperations.LOCATION_LIKE)
                        .value(city)
                        .build());
            }
        }
    }

    void buildCountryFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getLocation())) {
            val country = userFilter.getLocation().getCountry();
            if (!StringUtils.isBlank(country)) {
                filters.add(Filter.builder()
                        .aClass(String.class)
                        .field("country")
                        .operator(QueryOperations.LOCATION_LIKE)
                        .value(country)
                        .build());
            }
        }
    }

    void buildStreetFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getLocation())) {
            val street = userFilter.getLocation().getStreet();
            if (!StringUtils.isBlank(street)) {
                filters.add(Filter.builder()
                        .aClass(String.class)
                        .field("street")
                        .operator(QueryOperations.LOCATION_LIKE)
                        .value(street)
                        .build());
            }
        }
    }

    void buildHouseNumberFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getLocation())) {
            val houseNumber = userFilter.getLocation().getHouseNumber();
            if (!StringUtils.isBlank(houseNumber)) {
                filters.add(Filter.builder()
                        .aClass(String.class)
                        .field("houseNumber")
                        .operator(QueryOperations.LOCATION_EQUALS)
                        .value(houseNumber)
                        .build());
            }
        }
    }

    void buildReceiveOnAddressFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getNotificationSettings())) {
            val onAddress = userFilter.getNotificationSettings().getIsAllowedToReceiveOnAddress();
            if (onAddress != null) {
                filters.add(Filter.builder()
                        .aClass(Boolean.class)
                        .field("isAllowedToReceiveOnAddress")
                        .operator(QueryOperations.NOTIFICATION_EQUALS)
                        .value(onAddress)
                        .build());
            }
        }
    }


    void buildSendPromotionsMailingFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getNotificationSettings())) {
            val isPromMail = userFilter.getNotificationSettings().getIsAllowedToSendPromotionsAndMailingLists();
            if (isPromMail != null) {
                filters.add(Filter.builder()
                        .aClass(Boolean.class)
                        .field("isAllowedToSendDiscountsAndPromotionsMailingLists")
                        .operator(QueryOperations.NOTIFICATION_EQUALS)
                        .value(isPromMail)
                        .build());
            }
        }
    }

    void buildSendPopularArticlesFilter(@NotNull UserFilter userFilter, @NotNull List<Filter> filters) {
        if (Objects.nonNull(userFilter.getNotificationSettings())) {
            val isPopularArticles = userFilter.getNotificationSettings().getIsAllowedToSendPopularArticles();
            if (isPopularArticles != null) {
                filters.add(Filter.builder()
                        .aClass(Boolean.class)
                        .field("isAllowedToSendPopularArticles")
                        .operator(QueryOperations.NOTIFICATION_EQUALS)
                        .value(isPopularArticles)
                        .build());
            }
        }
    }
}