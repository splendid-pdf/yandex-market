package com.yandex.market.sellerinfoservice.service;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.mapper.SellerMapper;
import com.yandex.market.sellerinfoservice.model.Seller;
import com.yandex.market.sellerinfoservice.repository.SellerRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;

    @Transactional
    public UUID createSeller(SellerRequestDto sellerRequestDto) {
        if (sellerRepository.existsSellerByEmail(sellerRequestDto.email())) {
            throw new EntityExistsException("Seller with email " + sellerRequestDto.email() + " already exist");
        }
        Seller seller = sellerMapper.toSellerModel(sellerRequestDto);
        return sellerRepository.save(seller).getExternalId();
    }

    //На данный момент метод сделан чисто для теста, поэтому не Optional
    public Seller getSellerByExternalId(UUID sellerExternalId) {
        return sellerRepository.getSellerByExternalId(sellerExternalId);
    }
}