package com.yandex.market.shopservice.controllers.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.service.branch.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("${spring.application.url.branch}")
public class BranchController {

    private final BranchService branchService;


    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public BranchDto getBranchByExternalId(@PathVariable("externalId") UUID externalId) {
        return branchService.getBranchDtoByExternalId(externalId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createBranch(@RequestBody @Valid BranchDto branchDto) {
        log.info("Received a request to create new branch for shop system: %s" .formatted(branchDto));
        return branchService.createBranch(branchDto);
    }

    @PutMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateBranchByExternalId(@PathVariable("externalId") UUID externalId, @RequestBody @Valid BranchDto dto) {
        log.info("Received a request to update a branch by external id = %s" .formatted(externalId));
        branchService.updateBranchByExternalId(externalId, dto);
    }
}
