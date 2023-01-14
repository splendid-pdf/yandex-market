package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.*;
import com.yandex.market.shopservice.dto.projections.BranchResponseProjection;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponsesDto;
import com.yandex.market.shopservice.dto.shop.SpecialOfferDto;
import com.yandex.market.shopservice.dto.shop.SupportDto;
import com.yandex.market.shopservice.model.Location;
import com.yandex.market.shopservice.model.branch.*;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import com.yandex.market.shopservice.model.shop.Support;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ShopSystemMapper {

    // >>> shop

    public ShopSystem toShopSystemFromRequestDto(ShopSystemRequestDto dto) {
        return ShopSystem.builder()
                .name(dto.getName())
                .token(dto.getToken())
                .support(toSupportFromDto(dto.getSupport()))
                .legalEntityAddress(toLocationFromDto(dto.getLegalEntityAddress()))
                .specialOffers(toSpecialOffersFromDto(dto.getSpecialOffers()))
                .logoUrl(dto.getLogoUrl())
                .build();
    }

    public ShopSystemResponsesDto toShopSystemResponseDto(ShopSystem shopSystem) {
        return ShopSystemResponsesDto.builder()
                .name(shopSystem.getName())
                .token(shopSystem.getToken())
                .support(toSupportDto(shopSystem.getSupport()))
                .legalEntityAddress(toLocationDto(shopSystem.getLegalEntityAddress()))
                .specialOffers(toSpecialOffersDto(shopSystem.getSpecialOffers()))
                .logoUrl(shopSystem.getLogoUrl())
                .rating(shopSystem.getRating())
                .build();
    }

    // >>> special offer

    public Set<SpecialOffer> toSpecialOffersFromDto(Set<SpecialOfferDto> dto) {
        Set<SpecialOffer> specialOffers = new HashSet<>();
        if (dto != null && !dto.isEmpty()) {
            dto.forEach(specialOfferDto -> specialOffers.add(
                    SpecialOffer.builder()
                            .name(specialOfferDto.name())
                            .type(specialOfferDto.type())
                            .value(specialOfferDto.value())
                            .terms(specialOfferDto.terms())
                            .build()));
        }
        return specialOffers;
    }

    public Set<SpecialOfferDto> toSpecialOffersDto(Set<SpecialOffer> specialOffers) {
        Set<SpecialOfferDto> specialOffersDto = new HashSet<>();
        if (specialOffers != null && !specialOffers.isEmpty()) {
            specialOffers.forEach(specialOffer -> specialOffersDto.add(
                    new SpecialOfferDto(
                            specialOffer.getShopSystem().getExternalId(),
                            specialOffer.getName(),
                            specialOffer.getType(),
                            specialOffer.getValue(),
                            specialOffer.getTerms())));
        }
        return specialOffersDto;
    }

    // >>> support

    public SupportDto toSupportDto(Support support) {
        return new SupportDto(support.getNumber(), support.getEmail());
    }

    public Support toSupportFromDto(SupportDto dto) {
        return Support.builder()
                .number(dto.number())
                .email(dto.email())
                .build();
    }

    // >>> location

    public Location toLocationFromDto(LocationDto dto) {
        return Location.builder()
                .country(dto.country())
                .region(dto.region())
                .city(dto.city())
                .street(dto.street())
                .houseNumber(dto.houseNumber())
                .officeNumber(dto.officeNumber())
                .postcode(dto.postcode())
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
                location.getPostcode()
        );
    }

    // >>> branch

    public Branch toBranchFromDto(BranchDto dto) {
        return Branch.builder()
                .name(dto.getName())
                .token(dto.getToken())
                .ogrn(dto.getOgrn())
                .location(toLocationFromDto(dto.getLocation()))
                .contact(tocContactFromDto(dto.getContact()))
                .delivery(toDeliveryFromDto(dto.getDelivery()))
                .paymentMethods(dto.getPaymentMethods())
                .build();
    }

    private Contact tocContactFromDto(ContactDto contact) {
        return Contact.builder()
                .hotlinePhone(contact.hotlinePhone())
                .servicePhone(contact.servicePhone())
                .email(contact.email())
                .build();
    }

    public Delivery toDeliveryFromDto(DeliveryDto dto) {
        Set<DeliveryZoneDto> zoneDto = dto.getDeliveryZones();
        Set<DeliveryIntervalDto> intervalDto = dto.getDeliveryIntervals();
        Delivery delivery = Delivery.builder()
                .hasDelivery(dto.isHasDelivery())
                .hasExpressDelivery(dto.isHasExpressDelivery())
                .hasDeliveryToPickupPoint(dto.isHasDeliveryToPickupPoint())
                .pickupPointPartners(dto.getPickupPointPartners())
                .build();
        for (DeliveryZoneDto zone : zoneDto) {
            delivery.addDeliveryZone(toDeliveryZoneFromDto(zone));
        }
        for (DeliveryIntervalDto internal : intervalDto) {
            delivery.addDeliveryInterval(toDeliveryIntervalFromDto(internal));
        }
        return delivery;
    }

    private DeliveryZone toDeliveryZoneFromDto(DeliveryZoneDto zone) {
        return DeliveryZone.builder()
                .zoneId(zone.zoneId())
                .standardDeliveryPrice(zone.standardDeliveryPrice())
                .expressDeliveryPrice(zone.expressDeliveryPrice())
                .radiusInMeters(zone.radiusInMeters())
                .build();
    }

    private DeliveryInterval toDeliveryIntervalFromDto(DeliveryIntervalDto internal) {
        return DeliveryInterval.builder()
                .intervalId(internal.intervalId())
                .periodStart(internal.periodStart())
                .periodEnd(internal.periodEnd())
                .build();
    }

    private DeliveryResponseDto toDeliveryResponseDto(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .pickupPointPartners(delivery.getPickupPointPartners())
                .deliveryZones(toSetDeliveryZoneDto(delivery.getDeliveryZones()))
                .deliveryIntervals(toSetDeliveryIntervalDto(delivery.getDeliveryIntervals()))
                .build();
    }

    private Set<DeliveryIntervalDto> toSetDeliveryIntervalDto(Set<DeliveryInterval> deliveryIntervals) {
        Set<DeliveryIntervalDto> deliveryIntervalDto = new HashSet<>();
        for (DeliveryInterval intervalDto : deliveryIntervals) {
            deliveryIntervalDto.add(toDeliveryIntervalDto(intervalDto));
        }
        return deliveryIntervalDto;
    }

    private DeliveryIntervalDto toDeliveryIntervalDto(DeliveryInterval interval) {
        return DeliveryIntervalDto.builder()
                .intervalId(interval.getIntervalId())
                .periodStart(interval.getPeriodStart())
                .periodEnd(interval.getPeriodEnd())
                .build();
    }

    private Set<DeliveryZoneDto> toSetDeliveryZoneDto(Set<DeliveryZone> deliveryZones) {
        Set<DeliveryZoneDto> deliveryZonesDto = new HashSet<>();
        for (DeliveryZone zoneDto : deliveryZones) {
            deliveryZonesDto.add(toDeliveryZoneDto(zoneDto));
        }
        return deliveryZonesDto;
    }

    private DeliveryZoneDto toDeliveryZoneDto(DeliveryZone zoneDto) {
        return DeliveryZoneDto.builder()
                .zoneId(zoneDto.getZoneId())
                .radiusInMeters(zoneDto.getRadiusInMeters())
                .standardDeliveryPrice(zoneDto.getStandardDeliveryPrice())
                .expressDeliveryPrice(zoneDto.getExpressDeliveryPrice())
                .build();
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

//    >>> Projection
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
                .location(toLocationDto(branch.getLocation()))
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
                .delivery(toDeliveryResponseDto(branch.getDelivery()))
                .build();
    }
}
