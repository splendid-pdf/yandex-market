package com.yandex.market.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.request.SpecialPriceRequest;
import com.yandex.market.productservice.exception.InvalidCharacteristicsException;
import com.yandex.market.productservice.exception.SpecialPriceCreatingException;
import com.yandex.market.productservice.exception.TooMuchMainImagesException;
import com.yandex.market.productservice.model.*;
import com.yandex.market.productservice.repository.TypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.*;

@Service
@RequiredArgsConstructor
public class Validator {

    private final TypeRepository  typeRepository;

    private final Map<UUID, Map<String, ValueType>> typeCharacteristicsMap = new HashMap<>();

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void postConstruct() {
        typeRepository.findAllFetch()
                .forEach(type -> typeCharacteristicsMap.put(type.getExternalId(), type.getTypeCharacteristics()
                        .stream()
                        .collect(Collectors.toMap(TypeCharacteristic::getName, TypeCharacteristic::getValueType))));
    }

    public void validateProductCharacteristics(Product product) {
        UUID typeId = product.getType().getExternalId();

        List<String> exceptions = new ArrayList<>();
        List<ProductCharacteristic> requestedCharacteristics = product.getProductCharacteristics();
        Map<String, ValueType> requiredCharacteristics = typeCharacteristicsMap.get(typeId);

        int requiredSize = requiredCharacteristics.size();
        int requestedSize = requestedCharacteristics.size();

        if(requiredSize != requestedSize) {
           exceptions.add(String.format(INVALID_CHARACTERISTICS_SIZE, requiredSize, requestedSize));
        }

        for(ProductCharacteristic requestedCharacteristic : requestedCharacteristics) {
            validateCharacteristic(requestedCharacteristic, requiredCharacteristics, exceptions);
        }

        if(!exceptions.isEmpty()) {
            try {
                throw new InvalidCharacteristicsException(objectMapper.writeValueAsString(exceptions));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void validateImages(List<ProductImageDto> productImages) {
        long mainImagesCount = productImages.stream()
                .filter(ProductImageDto::isMain)
                .count();
        if(mainImagesCount > 1) {
            throw new TooMuchMainImagesException("Too much main images");
        }
    }

    public void validateSpecialPrice(SpecialPriceRequest specialPriceRequest) {
        LocalDateTime from = specialPriceRequest.fromDate();
        LocalDateTime to = specialPriceRequest.toDate();
        if(from.isAfter(to)) {
            throw new SpecialPriceCreatingException("from date cannot be after to date");
        }
    }

    private void validateCharacteristic(
                                        ProductCharacteristic requestedCharacteristic,
                                        Map<String, ValueType> requiredCharacteristics,
                                        List<String> exceptions
    ) {
        String name = requestedCharacteristic.getName();
        String value = requestedCharacteristic.getValue();
        ValueType requestedValueType = requestedCharacteristic.getValueType();
        ValueType requiredValueType = requiredCharacteristics.get(name);

        if(!requiredCharacteristics.containsKey(name)) {
            exceptions.add(String.format(INVALID_CHARACTERISTIC, name, name));
            return;
        }

        if(requiredCharacteristics.get(name) != requestedValueType) {
            exceptions.add(
                    String.format(INVALID_CHARACTERISTIC_TYPE, name, requiredValueType, requestedValueType)
            );
        }

        validateValueType(requiredValueType, name, value, exceptions);
    }

    private void validateValueType(ValueType valueType, String name, String value, List<String> exceptions) {
        try {
            valueType.parse(value);
        }
        catch (Exception ex) {
            exceptions.add(String.format(INVALID_CHARACTERISTIC_VALUE, name, value, valueType));
        }
    }

}

