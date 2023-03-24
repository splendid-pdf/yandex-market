package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.projections.TypePreview;
import com.yandex.market.productservice.dto.response.TypeResponse;
import com.yandex.market.productservice.mapper.TypeMapper;
import com.yandex.market.productservice.model.Room;
import com.yandex.market.productservice.model.Type;
import com.yandex.market.productservice.repository.RoomRepository;
import com.yandex.market.productservice.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    private final TypeMapper typeMapper;

    private final RoomRepository roomRepository;

    public TypeResponse getTypeById(UUID typeId) {
        Type type = typeRepository.findByExternalId(typeId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Type was not found by external id = %s", typeId)));
        return typeMapper.toTypeResponse(type);
    }

    public List<TypePreview> getAllTypes(Pageable pageable) {
        return typeRepository.findAllTypePreviews(pageable);
    }

    public List<TypePreview> getAllTypesByRoomId(UUID roomId, Pageable pageable) {
        roomRepository.findByExternalId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room was not found by external id = %s"));
        return typeRepository.findAllTypePreviewsByRoomId(roomId, pageable);
    }
}
