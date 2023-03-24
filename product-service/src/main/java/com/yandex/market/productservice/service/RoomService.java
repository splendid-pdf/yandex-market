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

import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.ROOM_NOT_FOUND_ERROR_MESSAGE;
import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.TYPE_NOT_FOUND_ERROR_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;


    public RoomPreview getRoomById(UUID roomId) {
        return roomRepository.findPreviewByExternalId(roomId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ROOM_NOT_FOUND_ERROR_MESSAGE, roomId)));
    }

    public List<RoomPreview> getAllRooms(Pageable pageable) {
        return roomRepository.findAllRoomPreviews(pageable);

    }

    public List<RoomPreview> getAllRoomsByTypeId(UUID typeId, Pageable pageable) {
        typeRepository.findByExternalId(typeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TYPE_NOT_FOUND_ERROR_MESSAGE, typeId)));
        return roomRepository.findAllRoomPreviewsByTypeId(typeId, pageable);
    }
}

