package com.yandex.market.shopservice.dto.branch;

import lombok.Builder;

@Builder
public record ContactResponseDto(
        String companyHotlinePhone,
        String companyEmail,
        String branchHotlinePhone,
        String branchServicePhone,
        String branchEmail) {
}
