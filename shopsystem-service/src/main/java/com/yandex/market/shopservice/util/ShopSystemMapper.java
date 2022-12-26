package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.BranchResponseDto;
import com.yandex.market.shopservice.dto.branch.ContactDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemBranchInfoDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponsesDto;
import com.yandex.market.shopservice.dto.shop.SupportDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.branch.Contact;
import com.yandex.market.shopservice.model.shop.Location;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.model.shop.Support;
import org.springframework.stereotype.Component;

@Component
public class ShopSystemMapper {
    public ShopSystem toShopSystemFromRequestDto(ShopSystemRequestDto dto) {
        return ShopSystem.builder()
                .name(dto.getName())
                .token(dto.getToken())
                .support(toSupportFromDto(dto.getSupport()))
                .legalEntityAddress(toLocationFromDto(dto.getLegalEntityAddress()))
                .specialOffers(dto.getSpecialOffers())
                .branches(dto.getBranches())
                .logoUrl(dto.getLogoUrl())
                .build();
    }

    public ShopSystemResponsesDto toShopSystemResponseDto(ShopSystem shopSystem) {
        return ShopSystemResponsesDto.builder()
                .name(shopSystem.getName())
                .token(shopSystem.getToken())
                .support(toSupportDto(shopSystem.getSupport()))
                .legalEntityAddress(toLocationDto(shopSystem.getLegalEntityAddress()))
                .specialOffers(shopSystem.getSpecialOffers())
                .branches(shopSystem.getBranches())
                .logoUrl(shopSystem.getLogoUrl())
                .rating(shopSystem.getRating())
                .build();
    }

    public SupportDto toSupportDto(Support support) {
        return new SupportDto(support.getNumber(), support.getEmail());
    }

    public Support toSupportFromDto(SupportDto dto) {
        return Support.builder()
                .number(dto.number())
                .email(dto.email())
                .build();
    }

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

    public Branch toBranchFromDto(BranchDto dto) {
        return Branch.builder()
                .name(dto.getName())
                .token(dto.getToken())
                .ogrn(dto.getOgrn())
                .location(toLocationFromDto(dto.getLocation()))
                .contact(tocContactFromDto(dto.getContact()))
                .delivery(dto.getDelivery())
                .build();
    }

    public Contact tocContactFromDto(ContactDto dto) {
        return Contact.builder()
                .hotlinePhone(dto.hotlinePhone())
                .servicePhone(dto.servicePhone())
                .email(dto.email())
                .build();
    }

    private ContactDto tocContactDto(Contact contact) {
        return new ContactDto(
                contact.getHotlinePhone(),
                contact.getServicePhone(),
                contact.getEmail()
        );
    }

    public BranchDto toBranchDto(Branch branch) {
        return BranchDto.builder()
                .name(branch.getName())
                .token(branch.getToken())
                .ogrn(branch.getOgrn())
                .location(toLocationDto(branch.getLocation()))
                .contact(tocContactDto(branch.getContact()))
                .delivery(branch.getDelivery())
                .build();
    }



    public BranchResponseDto toBranchDtoResponse(Branch branch, ShopSystemBranchInfoDto shopSystem){
        return BranchResponseDto.builder()
                .shopSystemExternalId(shopSystem.getShopSystemExternalId())
                .branchExternalId(branch.getExternalId())
                .companyName(shopSystem.getCompanyName())
                .branchName(branch.getName())
                .companyLogoUrl(shopSystem.getCompanyLogoUrl())
                .ogrn(branch.getOgrn())
                .location(toLocationDto(branch.getLocation()))
                .contact(tocContactDto(branch.getContact()))
                .delivery(branch.getDelivery())
                .build();
    }
}
