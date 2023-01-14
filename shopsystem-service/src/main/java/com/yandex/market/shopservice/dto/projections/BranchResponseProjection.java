package com.yandex.market.shopservice.dto.projections;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.ContactResponseDto;
import com.yandex.market.shopservice.dto.branch.DeliveryResponseDto;
import com.yandex.market.shopservice.model.branch.SupportedPaymentMethods;

import java.util.UUID;

public interface BranchResponseProjection {
    UUID getShopSystemExternalId();

    UUID getBranchExternalId();

    String getCompanyName();

    String getBranchName();

    String getCompanyLogoUrl();

    String getOgrn();

    LocationDto getLocation();

    ContactResponseDto getContact();

    SupportedPaymentMethods getPaymentMethods();

    boolean getHasDelivery();

    boolean getHasExpressDelivery();

    boolean getHasDeliveryToPickupPoint();

    DeliveryResponseDto getDelivery();
}
