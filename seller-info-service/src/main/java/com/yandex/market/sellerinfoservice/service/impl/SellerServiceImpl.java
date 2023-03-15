package com.yandex.market.sellerinfoservice.service.impl;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.sellerinfoservice.mapper.SellerMapper;
import com.yandex.market.sellerinfoservice.model.Seller;
import com.yandex.market.sellerinfoservice.repository.SellerRepository;
import com.yandex.market.sellerinfoservice.service.SellerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;

    private final String SELLER_NOT_FOUND_EXCEPTION = "Seller not found with seller id = ";

    @Override
    @Transactional
    public UUID createSeller(SellerRequestDto sellerRequestDto) {
        if (sellerRepository.existsSellerByEmail(sellerRequestDto.email())) {
            throw new EntityExistsException("Seller with email " + sellerRequestDto.email() + " already exist");
        }
        Seller seller = sellerMapper.toSellerModel(sellerRequestDto);
        return sellerRepository.save(seller).getExternalId();
    }

    @Override
    public SellerResponseDto getSellerByExternalId(UUID sellerExternalId) {
        return sellerMapper.toSellerResponseDto(sellerRepository
                .findByExternalId(sellerExternalId)
                .orElseThrow(() -> new EntityNotFoundException(SELLER_NOT_FOUND_EXCEPTION + sellerExternalId)));
    }

    @Override
    @Transactional
    public SellerResponseDto updateSellerWithDto(UUID sellerId, SellerRequestDto sellerRequestDto) {
        Seller seller = sellerRepository
                .findByExternalId(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(SELLER_NOT_FOUND_EXCEPTION + sellerId));
        sellerMapper.updateSellerModel(sellerRequestDto, seller);
        return sellerMapper.toSellerResponseDto(seller);
    }

    @Override
    @Transactional
    public void deleteSellerByExternalId(UUID sellerExternalId) {
        Seller seller = sellerRepository
                .findByExternalId(sellerExternalId)
                .orElseThrow(() -> new EntityNotFoundException(SELLER_NOT_FOUND_EXCEPTION + sellerExternalId));
        sellerRepository.deleteById(seller.getId());
    }
}