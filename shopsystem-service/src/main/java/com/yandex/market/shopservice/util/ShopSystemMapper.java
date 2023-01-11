package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.*;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponseDto;
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
                .branches(toBranchesFromDto(dto.getBranches()))
                .logoUrl(dto.getLogoUrl())
                .build();
    }

    public ShopSystemResponseDto toShopSystemResponseDto(ShopSystem shopSystem) {
        return ShopSystemResponseDto.builder()
                .name(shopSystem.getName())
                .token(shopSystem.getToken())
                .support(toSupportDto(shopSystem.getSupport()))
                .legalEntityAddress(toLocationDto(shopSystem.getLegalEntityAddress()))
                .specialOffers(toSpecialOffersDto(shopSystem.getSpecialOffers()))
                .branches(toBranchesDto(shopSystem.getBranches()))
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
                .build();
    }

    public BranchDto toBranchDto(Branch branch) {
        return BranchDto.builder()
                .shopSystem(branch.getShopSystem().getExternalId())
                .name(branch.getName())
                .token(branch.getToken())
                .ogrn(branch.getOgrn())
                .location(toLocationDto(branch.getLocation()))
                .contact(toContactDto(branch.getContact()))
                .delivery(toDeliveryDto(branch.getDelivery()))
                .build();
    }

    public Set<Branch> toBranchesFromDto(Set<BranchDto> dto) {
        Set<Branch> branches = new HashSet<>();
        if (dto != null && !dto.isEmpty()) {
            dto.forEach(branchDto -> branches.add(toBranchFromDto(branchDto)));
        }
        return branches;
    }

    public Set<BranchDto> toBranchesDto(Set<Branch> branches) {
        Set<BranchDto> branchesDto = new HashSet<>();
        if (branches != null && !branches.isEmpty()) {
            branches.forEach(branch -> branchesDto.add(toBranchDto(branch)));
        }
        return branchesDto;
    }

    // >>> contact

    public Contact tocContactFromDto(ContactDto dto) {
        return Contact.builder()
                .hotlinePhone(dto.hotlinePhone())
                .servicePhone(dto.servicePhone())
                .email(dto.email())
                .build();
    }

    public ContactDto toContactDto(Contact contact) {
        return new ContactDto(
                contact.getHotlinePhone(),
                contact.getServicePhone(),
                contact.getEmail()
        );
    }

    // >>> delivery

    public Delivery toDeliveryFromDto(DeliveryDto dto) {
        return Delivery.builder()
                .hasDelivery(dto.isHasDelivery())
                .hasExpressDelivery(dto.isHasExpressDelivery())
                .hasDeliveryToPickupPoint(dto.isHasDeliveryToPickupPoint())
                .pickupPointPartners(dto.getPickupPointPartners())
                .deliveryZones(toDeliveryZoneFromDto(dto.getDeliveryZones()))
                .deliveryIntervals(toDeliveryIntervalFromDto(dto.getDeliveryIntervals()))
                .build();
    }

    public DeliveryDto toDeliveryDto(Delivery delivery) {
        return DeliveryDto.builder()
                .branch(delivery.getBranch().getExternalId())
                .hasDelivery(delivery.isHasDelivery())
                .hasExpressDelivery(delivery.isHasExpressDelivery())
                .hasDeliveryToPickupPoint(delivery.isHasDeliveryToPickupPoint())
                .pickupPointPartners(delivery.getPickupPointPartners())
                .deliveryZones(toDeliveryZoneDto(delivery.getDeliveryZones()))
                .deliveryIntervals(toDeliveryIntervalDto(delivery.getDeliveryIntervals()))
                .build();
    }

    /// >>> delivery zone

    public Set<DeliveryZone> toDeliveryZoneFromDto(Set<DeliveryZoneDto> dto) {
        Set<DeliveryZone> deliveryZones = new HashSet<>();
        if (dto != null && !dto.isEmpty()) {
            dto.forEach(deliveryZoneDto -> deliveryZones.add(
                    DeliveryZone.builder()
                            .zoneId(deliveryZoneDto.zoneId())
//                            .delivery(toDeliveryFromDto(deliveryZoneDto.delivery()))
                            .radiusInMeters(deliveryZoneDto.radiusInMeters())
                            .standardDeliveryPrice(deliveryZoneDto.standardDeliveryPrice())
                            .expressDeliveryPrice(deliveryZoneDto.expressDeliveryPrice())
                            .build()));
        }
        return deliveryZones;
    }

    public Set<DeliveryZoneDto> toDeliveryZoneDto(Set<DeliveryZone> deliveryZones) {
        Set<DeliveryZoneDto> deliveryZonesDto = new HashSet<>();
        if (deliveryZones != null && !deliveryZones.isEmpty()) {
            deliveryZones.forEach(deliveryZone -> deliveryZonesDto.add(
                    new DeliveryZoneDto(
                            deliveryZone.getZoneId(),
//                            toDeliveryDto(deliveryZone.getDelivery()),
                            deliveryZone.getRadiusInMeters(),
                            deliveryZone.getStandardDeliveryPrice(),
                            deliveryZone.getExpressDeliveryPrice())));
        }
        return deliveryZonesDto;
    }

    // >>> delivery interval

    public Set<DeliveryInterval> toDeliveryIntervalFromDto(Set<DeliveryIntervalDto> dto) {
        Set<DeliveryInterval> deliveryIntervals = new HashSet<>();
        if (dto != null && !dto.isEmpty()) {
            dto.forEach(deliveryIntervalDto -> deliveryIntervals.add(
                    DeliveryInterval.builder()
//                            .delivery(toDeliveryFromDto(deliveryIntervalDto.delivery()))
                            .intervalId(deliveryIntervalDto.intervalId())
                            .periodStart(deliveryIntervalDto.periodStart())
                            .periodEnd(deliveryIntervalDto.periodEnd())
                            .build()));
        }
        return deliveryIntervals;
    }

    public Set<DeliveryIntervalDto> toDeliveryIntervalDto(Set<DeliveryInterval> deliveryIntervals) {
        Set<DeliveryIntervalDto> deliveryIntervalsDto = new HashSet<>();
        if (deliveryIntervals != null && !deliveryIntervals.isEmpty()) {
            deliveryIntervals.forEach(deliveryInterval -> deliveryIntervalsDto.add(
                    new DeliveryIntervalDto(
//                            toDeliveryDto(deliveryInterval.getDelivery()),
                            deliveryInterval.getIntervalId(),
                            deliveryInterval.getPeriodStart(),
                            deliveryInterval.getPeriodEnd()
                    )));
        }
        return deliveryIntervalsDto;
    }
}
