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
import com.yandex.market.shopservice.model.shop.SpecialOfferType;
import com.yandex.market.shopservice.model.shop.Support;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShopSystemMappingTest {

    private final ShopSystemMapStructMapper mapper = ShopSystemMapStructMapper.INSTANCE;

    @Autowired
    ShopSystemRepository shopSystemRepository;

    @Test
    void fillShopSystemRepo() {
        shopSystemRepository.save(createShopSystem());
    }

    @Test
    void toShopSystemResponse() {
        ShopSystem shopSystem = createShopSystem();
        shopSystem.setRating(5f);

        UUID externalId = UUID.randomUUID();
        Branch branch = Branch.builder()
                .shopSystem(ShopSystem.builder().externalId(externalId).build())
                .name("splendid moscow")
                .token("cccccccc-cccc-cccc-cccc-cccccccccccc")
                .ogrn("orgn")
                .location(shopSystem.getLegalEntityAddress())
                .contact(new Contact("hotlinePhone", "servicePhone", "email"))
                .build();
        branch.setDelivery(Delivery.builder().branch(branch).build());
        shopSystem.setBranches(Set.of(branch));

        SpecialOffer specialOffer = SpecialOffer.builder()
                .shopSystem(ShopSystem.builder().externalId(externalId).build())
                .name("name")
                .type(SpecialOfferType.DISCOUNT)
                .value(5)
                .terms("terms")
                .build();
        shopSystem.setSpecialOffers(Set.of(specialOffer));

//        ShopSystemResponsesDto response = mapper.toShopSystemResponseDto(shopSystem);
        ShopSystemResponseDto response = mapper.toShopSystemResponse(shopSystem);

        // assert shopsystem fields
        assertThat(response.getName()).isEqualTo(shopSystem.getName());
        assertThat(response.getToken()).isEqualTo(shopSystem.getToken());
        assertThat(response.getLogoUrl()).isEqualTo(shopSystem.getLogoUrl());
        assertThat(response.getRating()).isEqualTo(shopSystem.getRating());

        // assert support fields
        Support support = shopSystem.getSupport();

        SupportDto supportDto = response.getSupport();

        assertThat(supportDto.email())
                .isEqualTo(support.getEmail());
        assertThat(supportDto.number())
                .isEqualTo(support.getNumber());

        // assert address fields
        Location location = shopSystem.getLegalEntityAddress();

        LocationDto locationDto = response.getLegalEntityAddress();

        assertThat(locationDto.country()).isEqualTo(location.getCountry());
        assertThat(locationDto.region()).isEqualTo(location.getRegion());
        assertThat(locationDto.city()).isEqualTo(location.getCity());
        assertThat(locationDto.street()).isEqualTo(location.getStreet());
        assertThat(locationDto.houseNumber()).isEqualTo(location.getHouseNumber());
        assertThat(locationDto.postcode()).isEqualTo(location.getPostcode());

        // assert branch fields
        BranchDto branchDto = response.getBranches().iterator().next();

        assertThat(branchDto.getShopSystem()).isEqualTo(branch.getShopSystem().getExternalId());
        assertThat(branchDto.getName()).isEqualTo(branch.getName());
        assertThat(branchDto.getOgrn()).isEqualTo(branch.getOgrn());

        // assert specialoffer fields
        SpecialOfferDto specialOfferDto = response.getSpecialOffers().iterator().next();

        assertThat(specialOfferDto.shopSystem()).isEqualTo(specialOffer.getShopSystem().getExternalId());
        assertThat(specialOfferDto.name()).isEqualTo(specialOffer.getName());
        assertThat(specialOfferDto.type()).isEqualTo(specialOffer.getType());
        assertThat(specialOfferDto.terms()).isEqualTo(specialOffer.getTerms());

    }

    private static ShopSystem createShopSystem() {
        ShopSystem shopSystem = ShopSystem.builder()
                .id(1L)
                .externalId(UUID.randomUUID())
                .name("splendid")
                .token("cccccccc-cccc-cccc-cccc-cccccccccccc")
                .support(new Support("88005553535", "splendid@pdf.io"))
                .legalEntityAddress(createLocation())
                .logoUrl("logo.logo")
                .rating(5f)
                .build();

        shopSystem.addBranch(createBranch());
        shopSystem.addSpecialOffer(createSpecialOffer());

        return shopSystem;
    }

    private static Location createLocation() {
        return Location.builder()
                .country("Russia")
                .region("Moscow region")
                .city("Moscow")
                .street("Poltavskay")
                .houseNumber("277C")
                .officeNumber("25G")
                .postcode("101000")
                .build();
    }

    private static SpecialOffer createSpecialOffer() {
        return SpecialOffer.builder()
                .id(1L)
                .name("name")
                .type(SpecialOfferType.DISCOUNT)
                .value(5)
                .terms("terms")
                .build();
    }

    private static Branch createBranch() {
        Branch branch = Branch.builder()
                .id(1L)
                .externalId(UUID.randomUUID())
                .name("splendid moscow")
                .token("cccccccc-cccc-cccc-cccc-cccccccccccc")
                .ogrn("orgn")
                .location(createLocation())
                .contact(new Contact("hotlinePhone", "servicePhone", "email"))
                .build();

        branch.setDelivery(createDelivery());

        return branch;
    }

    private static Delivery createDelivery() {
        Delivery delivery = Delivery.builder()
                .id(1L)
                .hasDelivery(true)
                .hasExpressDelivery(true)
                .hasDeliveryToPickupPoint(true)
                .pickupPointPartners(Set.of(PickupPointPartner.POST_OFFICE))
                .build();

        delivery.addDeliveryZone(createDeliveryZone());
        delivery.addDeliveryInterval(createDeliveryInterval());

        return delivery;
    }

    private static DeliveryZone createDeliveryZone() {
        return DeliveryZone.builder()
                .id(1L)
                .zoneId("zoneId")
                .radiusInMeters(5)
                .standardDeliveryPrice(10)
                .expressDeliveryPrice(20)
                .build();
    }

    private static DeliveryInterval createDeliveryInterval() {
        return DeliveryInterval.builder()
                .id(1L)
                .intervalId("intervalId")
                .periodStart(LocalTime.now())
                .periodEnd(LocalTime.now())
                .build();
    }

    private static ShopSystemRequestDto createShopSystemRequestDto() {
        ShopSystemRequestDto shopSystemRequestDto = ShopSystemRequestDto.builder()
                .name("Eldorado")
                .token(UUID.randomUUID().toString())
                .support(new SupportDto("88005553535", "splendid@pdf.io"))
                .legalEntityAddress(createLocationDto())
                .logoUrl("https://static.eldorado.ru/espa/l.42.0-ab-esp-5648.3-Ps0BSwVDAKQkPjSPyBVlu/static_spa/assets/logo.dc65dadd.svg")
                .build();

        shopSystemRequestDto.setBranches(Set.of(createBranchDto()));
        shopSystemRequestDto.setSpecialOffers(Set.of(createSpecialOfferDto()));

        return shopSystemRequestDto;
    }

    private static SpecialOfferDto createSpecialOfferDto() {
        return SpecialOfferDto.builder()
                .shopSystem(UUID.randomUUID())
                .name("name")
                .type(SpecialOfferType.DISCOUNT)
                .value(5)
                .terms("terms")
                .build();
    }

    private static BranchDto createBranchDto() {
        return BranchDto.builder()
                .shopSystem(UUID.randomUUID())
                .name("Eldorado Ohta Moll")
                .token("cccccccc-cccc-cccc-cccc-cccccccccccc")
                .ogrn("1227700339492")
                .location(LocationDto.builder()
                        .country("Russia")
                        .region("Moscow Region")
                        .city("Moscow")
                        .street("Brantovskaya d.")
                        .houseNumber("5C")
                        .officeNumber("12G")
                        .postcode("117218")
                        .build())
                .contact(new ContactDto("hotlinePhone", "servicePhone", "email"))
                .delivery(createDeliveryDto())
                .build();
    }

    private static DeliveryDto createDeliveryDto() {
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .branch(UUID.randomUUID())
                .hasDelivery(true)
                .hasExpressDelivery(true)
                .hasDeliveryToPickupPoint(true)
                .pickupPointPartners(Set.of(PickupPointPartner.POST_OFFICE))
                .build();

        DeliveryZoneDto zoneDto = DeliveryZoneDto.builder()
                .zoneId("zoneId")
//                .delivery(deliveryDto)
                .radiusInMeters(5)
                .standardDeliveryPrice(10)
                .expressDeliveryPrice(20)
                .build();

        DeliveryIntervalDto intervalDto = DeliveryIntervalDto.builder()
//                .delivery(deliveryDto)
                .intervalId("intervalId")
                .periodStart(LocalTime.now())
                .periodEnd(LocalTime.now())
                .build();

        deliveryDto.setDeliveryZones(Set.of(zoneDto));
        deliveryDto.setDeliveryIntervals(Set.of(intervalDto));

        return deliveryDto;
    }

    private static ShopSystemResponseDto createShopSystemResponseDto() {
        ShopSystemResponseDto responseDto = ShopSystemResponseDto.builder()
                .name("splendid")
                .token("cccccccc-cccc-cccc-cccc-cccccccccccc")
                .support(new SupportDto("88005553535", "splendid@pdf.io"))
                .legalEntityAddress(createLocationDto())
                .logoUrl("logo.logo")
                .rating(5f)
                .build();

        responseDto.setBranches(Set.of(createBranchDto()));
        responseDto.setSpecialOffers(Set.of(createSpecialOfferDto()));

        return responseDto;
    }

    private static LocationDto createLocationDto() {
        return LocationDto.builder()
                .country("Russia")
                .region("Moscow Region")
                .city("Moscow")
                .street("Krizhanovskaya")
                .houseNumber("5C")
                .officeNumber("12G")
                .postcode("117218")
                .build();
    }
}
