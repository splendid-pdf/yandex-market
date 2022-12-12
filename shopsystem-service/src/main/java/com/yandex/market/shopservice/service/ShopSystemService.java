package com.yandex.market.shopservice.service;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.extensions.ShopSystemDtoIsEmpty;
import com.yandex.market.shopservice.extensions.ShopSystemNotFound;
import com.yandex.market.shopservice.impl.IShopSystemService;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.util.ShopSystemUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShopSystemService implements IShopSystemService {
    private final ShopSystemRepository repository;
    private final ShopSystemUtil util;

    public List<ShopSystem> getAllShopSystems() {
        log.info("All ShopSystem entries are searched for and returned. " +
                "Success in returning the SS list." +
                "Error when an exception or server error occurs.");
        return repository.findAll();
    }

    @Transactional
    public void createShopSystem(ShopSystemDto dto) {
        log.info("An object of type ShopSystem is being saved. " +
                "Expecting to add an object to the database." +
                "Error when throwing exceptions.");
        if (dto == null) {
            log.error("REQUEST REJECTED. " +
                    "An empty object was given as input. " +
                    "Because an empty or invalid JSON file was passed");
            throw new ShopSystemDtoIsEmpty("An empty object was passed. Creation cancelled!");
        }
        ShopSystem shopSystem = util.convertDtoToShopSystem(dto);
        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
        log.info("REQUEST SUCCESSFUL. An organization named \"" + shopSystem.getName() +
                "\" has been added. ShopSystem = " + shopSystem);
    }

    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        log.info("Search by externalId. " +
                "Expected to return an object found in the database." +
                "Error if the object was not found or an invalid request was made.");
        ShopSystem shopSystem = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> {
                            log.error("REQUEST REJECTED. By externalId = \"" + externalId +
                                    "\" could not find a matching record.");
                            throw new ShopSystemNotFound("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Creation canceled!");
                        }
                );
        log.info("REQUEST SUCCESSFUL. Request received to search for ShopSystem by externalId = \"" + externalId +
                "\". An object of type ShopSystem was found=" + shopSystem.toString());
        return shopSystem;
    }

    @Transactional
    public void deleteSystemShopByExternalId(UUID externalId) {
        log.info("Delete by externalId. " +
                "Expected deletion of the object found in the database." +
                "Error if the object was not found or an invalid request was made.");
        ShopSystem shopSystem = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> {
                            log.error("REQUEST REJECTED. By externalId = \"" + externalId +
                                    "\" could not find a matching record.");
                            throw new ShopSystemNotFound("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Deletion canceled!");
                        }
                );
        shopSystem.setDisabled(true);
        log.info("REQUEST SUCCESSFUL. The object with the input externalId = \"" + externalId +
                "\" was found ShopSystem = \"" + shopSystem.getName() +
                "\" and successfully marked as deleted");
    }
}
