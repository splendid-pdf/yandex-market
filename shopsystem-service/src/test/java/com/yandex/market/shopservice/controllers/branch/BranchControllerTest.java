package com.yandex.market.shopservice.controllers.branch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.ContactDto;
import com.yandex.market.shopservice.dto.branch.DeliveryIntervalDto;
import com.yandex.market.shopservice.dto.branch.DeliveryZoneDto;
import com.yandex.market.shopservice.model.branch.*;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.BranchRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:insert_shopsystem.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BranchControllerTest {

    @Value("${spring.application.url.branch}")
    private String url;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopSystemRepository shopSystemRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @Transactional
    @Rollback(value = false)
    void createBranch() throws Exception {
        ShopSystem shopSystem = shopSystemRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        BranchDto branchDto = getBranchRequest();
        ;
        branchDto.setShopSystem(shopSystem.getExternalId());

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(branchDto))
        );

        List<Branch> branches = shopSystem.getBranches().stream()
                .sorted(Comparator.comparing(Branch::getId))
                .toList();

        result
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(branches.get(1).getExternalId().toString())));

        assertThat(branches).hasSize(2);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void updateBranchByExternalId() throws Exception {
        BranchDto branchDto = getBranchRequest();

        branchDto.setName("Splendid Super Shop");
        branchDto.setToken("0d200bfS05e66beS8fe9asdfaf4f6777");
        branchDto.setOgrn("6669911337888");
        branchDto.setContact(new ContactDto("8-800-250-34-34", "8-800-222-89-89", "Splendid@support.com"));

        Branch branch = branchRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(put(url + "/{externalId}", branch.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(branchDto))
                )
                .andExpect(status().isOk());

        assertThat(branch.getName()).isEqualTo("Splendid Super Shop");
        assertThat(branch.getToken()).isEqualTo("0d200bfS05e66beS8fe9asdfaf4f6777");
        assertThat(branch.getOgrn()).isEqualTo("6669911337888");
        assertThat(branch.getContact().getHotlinePhone()).isEqualTo("8-800-250-34-34");
        assertThat(branch.getContact().getServicePhone()).isEqualTo("8-800-222-89-89");
        assertThat(branch.getContact().getEmail()).isEqualTo("Splendid@support.com");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void updateDeliveryByBranchExternalId() throws Exception {
        BranchDto branchDto = getBranchRequest();

        Set<PickupPointPartner> pickupPointPartnersDto = branchDto.getDelivery().getPickupPointPartners();
        Set<@Valid DeliveryZoneDto> deliveryZonesDto = branchDto.getDelivery().getDeliveryZones();
        Set<@Valid DeliveryIntervalDto> deliveryIntervalsDto = branchDto.getDelivery().getDeliveryIntervals();

        pickupPointPartnersDto.add(PickupPointPartner.WILDBERRIES);

        DeliveryZoneDto deliveryZoneDto = new DeliveryZoneDto("id742100", 2500, 400, 600);
        deliveryZonesDto.remove(deliveryZoneDto);
        deliveryZonesDto.add(deliveryZoneDto);

        DeliveryIntervalDto deliveryIntervalDto = new DeliveryIntervalDto("126600302", LocalTime.of(9, 0), LocalTime.of(23, 0));
        deliveryIntervalsDto.remove(deliveryIntervalDto);
        deliveryIntervalsDto.add(deliveryIntervalDto);

        Branch branch = branchRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(put(url + "/{externalId}", branch.getExternalId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(branchDto))
                )
                .andExpect(status().isOk());

        Delivery delivery = branch.getDelivery();

        assertThat(delivery.getPickupPointPartners()).hasSize(4);
        assertThat(delivery.getPickupPointPartners()).contains(PickupPointPartner.WILDBERRIES);


        DeliveryZone updatedZone = delivery.getDeliveryZones().stream()
                .filter(z -> z.getZoneId().equals("id742100")).findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(updatedZone.getRadiusInMeters()).isEqualTo(2500);
        assertThat(updatedZone.getStandardDeliveryPrice()).isEqualTo(400);
        assertThat(updatedZone.getExpressDeliveryPrice()).isEqualTo(600);


        DeliveryInterval updatedInterval = delivery.getDeliveryIntervals().stream()
                .filter(i -> i.getIntervalId().equals("126600302")).findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(updatedInterval.getPeriodStart()).isEqualTo(LocalTime.of(9, 0));
        assertThat(updatedInterval.getPeriodEnd()).isEqualTo(LocalTime.of(23, 0));
    }

    private BranchDto getBranchRequest() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/CreateBranchRequest.json"));
        return mapper.readValue(json, BranchDto.class);
    }
}
