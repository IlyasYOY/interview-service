package com.github.interview.interviewservice.service;

import com.github.interview.interviewservice.controller.dto.CreateRoomResponseDot;
import com.github.interview.interviewservice.controller.dto.EnterRoomResponseDto;
import com.github.interview.interviewservice.controller.dto.GetRoomResponseDto;
import com.github.interview.interviewservice.controller.dto.UserDto;
import com.github.interview.interviewservice.controller.exception.ApplicationException;
import com.github.interview.interviewservice.controller.exception.ErrorType;
import com.github.interview.interviewservice.domain.Hasher;
import com.github.interview.interviewservice.domain.entity.Room;
import com.github.interview.interviewservice.domain.value.User;
import com.github.interview.interviewservice.model.RoomModel;
import com.github.interview.interviewservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class MongoRoomService implements RoomService {
    private final Converter<Room, RoomModel> roomRoomModelConverter;
    private final Converter<RoomModel, Room> roomModelRoomConverter;
    private final Converter<User, UserDto> userUserDtoConverter;
    private final RoomRepository roomRepository;
    private final Hasher hasher;
    private final Clock clock;

    @Override
    public Mono<CreateRoomResponseDot> create(String username) {
        Room room = Room.forUser(username).build();
        String creatorHash = room.getCreatorHash(hasher);

        if (creatorHash == null) {
            throw ApplicationException.of(ErrorType.CREATOR_WAS_NOT_SET, Map.of("room", room.getId()));
        }

        RoomModel roomModel = roomRoomModelConverter.convert(room);
        ZonedDateTime now = ZonedDateTime.now(clock);
        roomModel.setCreatedAt(now);
        roomModel.setUpdatedAt(now);
        roomModel.getUsers().forEach(userModel -> userModel.setCreatedAt(now));

        return roomRepository.save(roomModel)
                .map(it -> CreateRoomResponseDot.builder()
                        .roomId(it.getId())
                        .userHash(creatorHash)
                        .build());
    }

    @Override
    public Mono<EnterRoomResponseDto> addUser(String roomId, String username) {
        return roomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(ApplicationException.of(ErrorType.ROOM_WAS_NOT_FOUND,
                        Map.of("room", roomId))))
                .map(roomModelRoomConverter::convert)
                .map(room -> {
                    if (room.addUser(username)) {
                        return room.getUserHash(hasher, username);
                    }
                    throw ApplicationException.of(ErrorType.ROOM_ALREADY_HAS_THIS_USER,
                            Map.of("room", roomId, "user", username));
                })
                .filter(not(String::isBlank))
                .map(hash -> EnterRoomResponseDto.builder()
                        .roomId(roomId)
                        .userHash(hash)
                        .build());
    }

    @Override
    public Mono<GetRoomResponseDto> get(String hash) {
        return Room.getRoomForHash(hasher, hash,
                (String roomId) -> roomRepository.findById(roomId)
                        .map(roomModelRoomConverter::convert))
                .onErrorResume(throwable -> Mono.empty())
                .switchIfEmpty(Mono.error(ApplicationException.of(ErrorType.WRONG_HASH,
                        Map.of("hash", hash))))
                .map(room -> GetRoomResponseDto.builder()
                        .text(room.getText())
                        .users(room.getUsers().stream()
                                .map(userUserDtoConverter::convert)
                                .collect(Collectors.toList()))
                        // Here we cannot have NPE, cause we have ths user 100% sure
                        .you(userUserDtoConverter.convert(room.getUserByHash(hasher, hash)))
                        .build());
    }
}
