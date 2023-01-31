package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.branch.*;
import com.yandex.market.shopservice.dto.projections.BranchResponseProjection;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.branch.Contact;
import com.yandex.market.shopservice.model.branch.Delivery;
import com.yandex.market.shopservice.model.branch.SupportedPaymentMethods;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.model.shop.Support;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ShopSystemMapperClass {

    private final ShopSystemMapper mapper;

    public Delivery toDeliveryFromDto(DeliveryDto dto) {
        Delivery delivery = Delivery.builder()
                .hasDelivery(dto.isHasDelivery())
                .hasExpressDelivery(dto.isHasExpressDelivery())
                .hasDeliveryToPickupPoint(dto.isHasDeliveryToPickupPoint())
                .pickupPointPartners(dto.getPickupPointPartners())
                .build();
        for (DeliveryZoneDto zone : dto.getDeliveryZones()) {
            delivery.addDeliveryZone(mapper.toDeliveryZoneFromDto(zone));
        }
        for (DeliveryIntervalDto internal : dto.getDeliveryIntervals()) {
            delivery.addDeliveryInterval(mapper.toDeliveryIntervalFromDto(internal));
        }
        return delivery;
    }

    private ContactResponseDto toContactResponseDto(Contact contact, Support support) {
        return ContactResponseDto.builder()
                .branchEmail(contact.getEmail())
                .branchHotlinePhone(contact.getHotlinePhone())
                .companyEmail(support.getEmail())
                .companyHotlinePhone(support.getNumber())
                .branchServicePhone(contact.getServicePhone())
                .build();
    }

    public BranchResponseDto toBranchDtoResponse(BranchResponseProjection projection) {
        ShopSystem shopSystem = projection.getShopSystem();
        Branch branch = projection.getBranch();
        Delivery delivery = projection.getBranch().getDelivery();
        return BranchResponseDto.builder()
                .shopSystemExternalId(shopSystem.getExternalId())
                .branchExternalId(branch.getExternalId())
                .branchName(branch.getName())
                .companyName(shopSystem.getName())
                .companyLogoUrl(shopSystem.getLogoUrl())
                .ogrn(branch.getOgrn())
                .location(mapper.toLocationDto(branch.getLocation()))
                .contact(toContactResponseDto(
                        branch.getContact(),
                        shopSystem.getSupport()))
                .paymentMethods(SupportedPaymentMethods.builder()
                        .online(branch.getPaymentMethods().isOnline())
                        .cash(branch.getPaymentMethods().isCash())
                        .build())
                .hasDelivery(delivery.isHasDelivery())
                .hasExpressDelivery(delivery.isHasExpressDelivery())
                .hasDeliveryToPickupPoint(delivery.isHasDeliveryToPickupPoint())
                .delivery(mapper.toDeliveryResponseDto(branch.getDelivery()))
                .build();
    }
}