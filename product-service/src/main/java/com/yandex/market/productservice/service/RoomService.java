package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.projections.RoomPreview;
import com.yandex.market.productservice.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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

    public RoomPreview getRoomPreviewById(UUID roomId) {
        return roomRepository.findPreviewByExternalId(roomId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ROOM_NOT_FOUND_ERROR_MESSAGE, roomId)));
    }

    public List<RoomPreview> getRoomPreviews() {
        return roomRepository.findRoomPreviews(Sort.by("name"));

    }

    public List<RoomPreview> getRoomPreviewsByTypeId(UUID typeId) {
        List<RoomPreview> roomPreviews = roomRepository.findRoomPreviewsByTypeId(typeId, Sort.by("name"));
        if(roomPreviews.isEmpty()) {
            throw new EntityNotFoundException(String.format(TYPE_NOT_FOUND_ERROR_MESSAGE, typeId));
        }
        return roomPreviews;
    }
}

