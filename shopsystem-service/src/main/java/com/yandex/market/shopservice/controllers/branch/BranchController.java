package com.yandex.market.shopservice.controllers.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.service.branch.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.url.branch}")
public class BranchController {
    private final BranchService branchService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createBranch(@RequestBody @Valid BranchDto branchDto) {
        log.info("Received a request to create new branch for shop system: %s".formatted(branchDto));
        branchService.createBranch(branchDto);
    }
}
