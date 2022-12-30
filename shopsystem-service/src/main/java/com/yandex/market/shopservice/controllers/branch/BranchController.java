package com.yandex.market.shopservice.controllers.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.BranchResponseDto;
import com.yandex.market.shopservice.service.branch.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.url.branch}")
public class BranchController {
    private final BranchService branchService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createBranch(@RequestBody @Valid BranchDto branchDto) {
        log.info("Received a request to create new branch for shop system: %s".formatted(branchDto));
        return branchService.createBranch(branchDto);
    }

    @PutMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateBranchByExternalId(@PathVariable("externalId") UUID externalId, @RequestBody @Valid BranchDto dto) {
        log.info("Received a request to update a branch by external id = %s".formatted(externalId));
        branchService.updateBranchByExternalId(externalId, dto);
    }

//
//    @GetMapping("/search/{externalId}")
//    @ResponseStatus(HttpStatus.OK)
//    public Page<BranchResponseDto> getAllBranchesByShopSystemExternalId(@PageableDefault(size = 20) Pageable pageable,
//                                                              @PathVariable("externalId") UUID externalId){
//        log.info("Received a request to get paginated list of branches by shop systems.");
//        return branchService.getBranchesByShopSystem(externalId, pageable);
//    }

    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public BranchResponseDto getBranchByExternalId(@PathVariable("externalId") UUID externalId){
        log.info("Received a request to get branch by external id.");
        return branchService.getBranchResponseDtoByExternalId(externalId);
    }
}
