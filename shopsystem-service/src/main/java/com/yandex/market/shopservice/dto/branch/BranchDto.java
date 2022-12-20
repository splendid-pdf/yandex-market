package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.Contact;
import com.yandex.market.shopservice.model.branch.Delivery;
import com.yandex.market.shopservice.model.shop.Location;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {
    @NotBlank(message = "Field \"Name\" must not be empty")
    private String name;
    private String token;
    private String ogrn;

    private ShopSystem shopSystem;

    private Location location;

    private Contact contact;

    private Delivery delivery;
}
