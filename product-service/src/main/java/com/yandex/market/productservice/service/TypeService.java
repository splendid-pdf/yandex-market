package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.projections.TypePreview;
import com.yandex.market.productservice.dto.response.TypeResponse;
import com.yandex.market.productservice.mapper.TypeMapper;
import com.yandex.market.productservice.model.Type;
import com.yandex.market.productservice.repository.RoomRepository;
import com.yandex.market.productservice.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.ROOM_NOT_FOUND_ERROR_MESSAGE;
import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.TYPE_NOT_FOUND_ERROR_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    private final TypeMapper typeMapper;

    private final RoomRepository roomRepository;

    public TypeResponse getTypeById(UUID typeId) {
        Type type = typeRepository.findByExternalId(typeId)
                .orElseThrow(()-> new EntityNotFoundException(String.format(TYPE_NOT_FOUND_ERROR_MESSAGE, typeId)));
        return typeMapper.toTypeResponse(type);
    }

    public List<TypePreview> getAllTypes(Pageable pageable) {
        return typeRepository.findAllTypePreviews(pageable);
    }

    public List<TypePreview> getAllTypesByRoomId(UUID roomId, Pageable pageable) {
        roomRepository.findByExternalId(roomId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ROOM_NOT_FOUND_ERROR_MESSAGE, roomId)));
        return typeRepository.findAllTypePreviewsByRoomId(roomId, pageable);
    }
}
