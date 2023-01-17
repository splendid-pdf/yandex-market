package com.yandex.market.shopservice.service.shop;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.model.Location;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.service.shop.impl.ShopSystemServiceImpl;
import com.yandex.market.shopservice.util.ShopSystemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopSystemServiceImplTest {

    @Mock
    private ShopSystemRepository shopSystemRepository;

    @Mock
    private ShopSystemMapper shopSystemMapper;

    @InjectMocks
    private ShopSystemServiceImpl shopSystemService;

    @Test
    void createShopSystem() {
        ShopSystemRequestDto dto = ShopSystemRequestDto.builder().build();
        ShopSystem shopSystem = new ShopSystem();
        shopSystem.setLegalEntityAddress(new Location());

        when(shopSystemMapper.toShopSystem(any(ShopSystemRequestDto.class))).thenReturn(shopSystem);

        //todo add ExternalIdProvider to test returned externalId
        UUID shopSystemExternalId = shopSystemService.createShopSystem(dto);

        assertThat(shopSystemExternalId).isEqualTo(shopSystem.getExternalId());
        verify(shopSystemRepository, times(1)).save(shopSystem);
    }

    @Test
    void createShopSystemWithBranches() {
        ShopSystemRequestDto dto = ShopSystemRequestDto.builder().branches(Set.of(new BranchDto())).build();
        ShopSystem shopSystem = ShopSystem.builder().branches(Set.of(new Branch())).build();
        shopSystem.setLegalEntityAddress(new Location());

        when(shopSystemMapper.toShopSystem(any(ShopSystemRequestDto.class))).thenReturn(shopSystem);

        //todo add ExternalIdProvider to test returned externalId
        UUID shopSystemExternalId = shopSystemService.createShopSystem(dto);

        assertThat(shopSystemExternalId).isEqualTo(shopSystem.getExternalId());
        assertThat(shopSystem.getBranches()).allSatisfy(b -> assertThat(b.getExternalId()).isNotNull());
        verify(shopSystemRepository, times(1)).save(shopSystem);
    }

    @Test
    void deleteShopSystemByExternalIdDisableShopSystemAndAllItsBranches() {
        ShopSystem shopSystem = ShopSystem.builder().branches(Set.of(new Branch())).build();
        shopSystem.setLegalEntityAddress(new Location());

        when(shopSystemRepository.findByExternalId(any(UUID.class))).thenReturn(Optional.of(shopSystem));

        shopSystemService.deleteShopSystemByExternalId(UUID.randomUUID());

        assertThat(shopSystem.isDisabled()).isTrue();
        assertThat(shopSystem.getBranches()).allSatisfy(b -> assertThat(b.isDisabled()).isTrue());
        verify(shopSystemRepository, times(1)).findByExternalId(any(UUID.class));
    }
}
