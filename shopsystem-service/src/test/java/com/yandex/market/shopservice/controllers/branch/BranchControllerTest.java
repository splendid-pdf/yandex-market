package com.yandex.market.shopservice.controllers.branch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.ContactDto;
import com.yandex.market.shopservice.dto.branch.DeliveryIntervalDto;
import com.yandex.market.shopservice.dto.branch.DeliveryZoneDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.branch.PickupPointPartner;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.yandex.market.shopservice.model.branch.PickupPointPartner.WILDBERRIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:insert_shopsystem.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class BranchControllerTest {

    @Value("${spring.application.url.branch}")
    private String url;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopSystemRepository shopSystemRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getBranchByExternalId() throws Exception {

        ResultActions actions = mockMvc.perform(get(url + "/{externalId}", "6166b912-878c-11ed-a1eb-0242ac120002"))
                .andExpect(status().isOk());

        actions
                .andExpect(jsonPath("$.shopSystem").value("87ba7a03-054e-4c7f-ac35-f4802d66cec3"))
                .andExpect(jsonPath("$.token").value("0d305bfS05e74beS8fe9cedfaf4f6003"))
                .andExpect(jsonPath("$.ogrn").value("1227700339492"));

        actions
                .andExpect(jsonPath("$.location.country").value("Russia"))
                .andExpect(jsonPath("$.location.region").value("Moscow Region"))
                .andExpect(jsonPath("$.location.city").value("Saint-Petersburg"))
                .andExpect(jsonPath("$.location.street").value("Brantovskaya d."))
                .andExpect(jsonPath("$.location.houseNumber").value("5B"))
                .andExpect(jsonPath("$.location.officeNumber").value("12C"))
                .andExpect(jsonPath("$.location.postcode").value("117218"));

        actions
                .andExpect(jsonPath("$.contact.hotlinePhone").value("8(937)640-9999"))
                .andExpect(jsonPath("$.contact.servicePhone").value("8(939)640-8888"))
                .andExpect(jsonPath("$.contact.email").value("eldorado-ohta@eldorado.ru"));

        actions
                .andExpect(jsonPath("$.delivery.branch").value("6166b912-878c-11ed-a1eb-0242ac120002"))
                .andExpect(jsonPath("$.delivery.hasDelivery").value("true"))
                .andExpect(jsonPath("$.delivery.hasExpressDelivery").value("true"))
                .andExpect(jsonPath("$.delivery.hasDeliveryToPickupPoint").value("true"));

        actions
                .andExpect(jsonPath("$.delivery.pickupPointPartners").value(hasSize(3)))
                .andExpect(jsonPath("$.delivery.pickupPointPartners[*]").value(
                        hasItems("OZON", "YANDEX_MARKET", "WILDBERRIES")
                ));

        actions
                .andExpect(jsonPath("$.delivery.deliveryZones").value(hasSize(2)))
                .andExpect(jsonPath("$.delivery.deliveryZones[0].zoneId").value("id742101"))
                .andExpect(jsonPath("$.delivery.deliveryZones[0].radiusInMeters").value("5000"))
                .andExpect(jsonPath("$.delivery.deliveryZones[0].standardDeliveryPrice").value("799.0"))
                .andExpect(jsonPath("$.delivery.deliveryZones[0].expressDeliveryPrice").value("599.0"));

        actions
                .andExpect(jsonPath("$.delivery.deliveryIntervals").value(hasSize(2)))
                .andExpect(jsonPath("$.delivery.deliveryIntervals[0].intervalId").value("126600303"))
                .andExpect(jsonPath("$.delivery.deliveryIntervals[0].periodStart").value("12:00:00"))
                .andExpect(jsonPath("$.delivery.deliveryIntervals[0].periodEnd").value("18:00:00"));
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void createBranch() throws Exception {
        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        BranchDto branchDto = getBranchRequest();

        branchDto.setShopSystem(UUID.fromString("87ba7a03-054e-4c7f-ac35-f4802d66cec3"));

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(branchDto))
        );

        List<Branch> branches = shopSystem.getBranches().stream()
                .sorted(Comparator.comparing(Branch::getId))
                .toList();

        // todo тут нельзя протестировать без репозитория
        result
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(branches.get(1).getExternalId().toString())));

        assertThat(branches).hasSize(2);
    }

    @Test
    void updateBranchByExternalId() throws Exception {
        BranchDto branchDto = getBranchRequest();

        branchDto.setName("Splendid Super Shop");
        branchDto.setToken("0d200bfS05e66beS8fe9asdfaf4f6777");
        branchDto.setOgrn("6669911337888");

        branchDto.setContact(new ContactDto("8(937)640-9999", "8(939)640-8888", "Splendid@support.com"));

        mockMvc.perform(put(url + "/{externalId}", "6166b912-878c-11ed-a1eb-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(branchDto))
                )
                .andExpect(status().isOk());
    }

    @Test
    void updateDeliveryByBranchExternalId() throws Exception {
        BranchDto branchDto = getBranchRequest();

        Set<PickupPointPartner> pickupPointPartnersDto = branchDto.getDelivery().getPickupPointPartners();
        Set<@Valid DeliveryZoneDto> deliveryZonesDto = branchDto.getDelivery().getDeliveryZones();
        Set<@Valid DeliveryIntervalDto> deliveryIntervalsDto = branchDto.getDelivery().getDeliveryIntervals();

        pickupPointPartnersDto.add(WILDBERRIES);

        DeliveryZoneDto deliveryZoneDto = new DeliveryZoneDto("id742100", 2500, 400, 600);
        deliveryZonesDto.remove(deliveryZoneDto);
        deliveryZonesDto.add(deliveryZoneDto);

        DeliveryIntervalDto deliveryIntervalDto = new DeliveryIntervalDto("126600302", LocalTime.of(9, 0), LocalTime.of(23, 0));
        deliveryIntervalsDto.remove(deliveryIntervalDto);
        deliveryIntervalsDto.add(deliveryIntervalDto);

        mockMvc.perform(put(url + "/{externalId}", "6166b912-878c-11ed-a1eb-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(branchDto))
                )
                .andExpect(status().isOk());
    }

    private BranchDto getBranchRequest() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/CreateBranchRequest.json"));
        return mapper.readValue(json, BranchDto.class);
    }
}
