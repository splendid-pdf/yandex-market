package com.yandex.market.shopservice.service.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.BranchRepository;
import com.yandex.market.shopservice.service.branch.impl.BranchServiceImpl;
import com.yandex.market.shopservice.service.shop.ShopSystemService;
import com.yandex.market.shopservice.util.BranchMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ShopSystemService shopSystemService;

    @Mock
    private BranchMapper branchMapper;

    @InjectMocks
    private BranchServiceImpl branchService;

    @Test
    void returnExternalIdWhenBranchIsCreated() {
        Branch branch = Branch.builder().id(1L).build();
        ShopSystem shopSystem = ShopSystem.builder().id(1L).build();

        when(branchMapper.toBranch(any(BranchDto.class))).thenReturn(branch);
        when(shopSystemService.getShopSystemByExternalId(any())).thenReturn(shopSystem);

        //todo add ExternalIdProvider to test returned externalId
        UUID externalId = branchService.createBranch(new BranchDto());

        assertThat(shopSystem.getBranches()).containsExactly(branch);
        assertThat(branch.getShopSystem()).isEqualTo(shopSystem);

        verify(branchRepository, times(1)).save(branch);
    }

    @Test
    void throwEntityNotFoundExceptionWhenBranchExternalIdNotExists() {
        UUID uuid = UUID.randomUUID();
        String msg = "Branch by given externalId = \"" + uuid + "\" was not found. Search canceled!";

        when(branchRepository.findByExternalId(uuid)).thenThrow(new EntityNotFoundException(msg));

        assertThatThrownBy(() -> branchService.getBranchDtoByExternalId(uuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(msg);
    }
}
