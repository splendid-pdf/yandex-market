package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.projections.RoomPreview;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;


    public RoomPreview getRoomById(UUID roomId) {
        return roomRepository.findPreviewByExternalId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room was not found by id = %s"));
    }

    public Page<RoomPreview> getAllRooms(Pageable pageable) {
        return roomRepository.findAllRoomPreviews(pageable);

    }

    public Page<RoomPreview> getAllRoomsByTypeId(UUID typeId, Pageable pageable) {
        typeRepository.findByExternalId(typeId)
                .orElseThrow(() -> new EntityNotFoundException("Type was not found by external id = %s"));
        return roomRepository.findAllRoomPreviewsByTypeId(typeId, pageable);
    }
}

