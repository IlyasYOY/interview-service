package com.github.interview.interviewservice.service;

import com.github.interview.interviewservice.controller.dto.RoomCreatedDto;
import com.github.interview.interviewservice.controller.exception.ApplicationException;
import com.github.interview.interviewservice.controller.exception.ErrorType;
import com.github.interview.interviewservice.domain.Hasher;
import com.github.interview.interviewservice.domain.entity.Room;
import com.github.interview.interviewservice.model.RoomModel;
import com.github.interview.interviewservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MongoRoomService implements RoomService {
    private final Converter<Room, RoomModel> roomConverter;
    private final RoomRepository roomRepository;
    private final Hasher hasher;
    private final Clock clock;

    @Override
    public Mono<RoomCreatedDto> create(String username) {
        Room room = Room.forUser(username).build();
        String creatorHash = room.getCreatorHash(hasher);

        if (creatorHash == null) {
            throw ApplicationException.of(ErrorType.CREATOR_WAS_NOT_SET, Map.of("room", room.getId()));
        }

        RoomModel roomModel = roomConverter.convert(room);
        ZonedDateTime now = ZonedDateTime.now(clock);
        roomModel.setCreatedAt(now);
        roomModel.setUpdatedAt(now);
        roomModel.getUsers().forEach(userModel -> userModel.setCreatedAt(now));

        Mono<RoomModel> savedRoomModel = roomRepository.save(roomModel);

        return savedRoomModel
                .map(it -> RoomCreatedDto.builder()
                        .roomId(it.getId())
                        .userHash(creatorHash)
                        .build());
    }
}
