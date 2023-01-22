package com.yandex.market.userinfoservice.specification;

import org.apache.logging.log4j.util.TriConsumer;
import org.openapitools.api.model.UserFilter;

import java.util.List;

public enum UserFieldFiltration {
    //StringType
    FIRST_NAME(FilterOrganizer::buildFirstNameFilter),
    LAST_NAME(FilterOrganizer::buildLastNameFilter),
    SEX(FilterOrganizer::buildSexFilter),
    CITY(FilterOrganizer::buildCityFilter),
    COUNTRY(FilterOrganizer::buildCountryFilter),
    STREET(FilterOrganizer::buildStreetFilter),
    HOUSE_NUMBER(FilterOrganizer::buildHouseNumberFilter),

    //LocalDateType
    BIRTHDAY_FROM(FilterOrganizer::buildBirthdayFromFilter),
    BIRTHDAY_TO(FilterOrganizer::buildBirthdayToFilter),

    //BooleanType
    TO_RECEIVE_ON_ADDRESS(FilterOrganizer::buildReceiveOnAddressFilter),
    TO_SEND_PROMOTIONS_AND_MAILING(FilterOrganizer::buildSendPromotionsMailingFilter),
    TO_SEND_POPULAR_ARTICLES(FilterOrganizer::buildSendPopularArticlesFilter);

    public final TriConsumer<FilterOrganizer, UserFilter, List<Filter>> buildFilter;

    UserFieldFiltration(TriConsumer<FilterOrganizer, UserFilter, List<Filter>> buildFilter){
        this.buildFilter = buildFilter;
    }
}