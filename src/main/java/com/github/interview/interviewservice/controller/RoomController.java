package com.github.interview.interviewservice.controller;

import com.github.interview.interviewservice.controller.dto.CreateRoomRequestDto;
import com.github.interview.interviewservice.controller.dto.CreateRoomResponseDot;
import com.github.interview.interviewservice.controller.dto.EnterRoomRequestDto;
import com.github.interview.interviewservice.controller.dto.EnterRoomResponseDto;
import com.github.interview.interviewservice.controller.dto.GetRoomResponseDto;
import com.github.interview.interviewservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(RoomController.ROOT_URL)
public class RoomController {

    public static final String ROOT_URL = "/rooms";

    private final RoomService roomService;

    @PostMapping
    public Mono<ResponseEntity<CreateRoomResponseDot>> create(
            @Valid @RequestBody CreateRoomRequestDto createRoomRequestDto
    ) {
        String username = createRoomRequestDto.getUsername();
        Mono<CreateRoomResponseDot> roomCreatedDtoMono = roomService.create(username);
        return roomCreatedDtoMono
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{roomId}")
    public Mono<ResponseEntity<EnterRoomResponseDto>> enter(
            @PathVariable String roomId,
            @Valid @RequestBody EnterRoomRequestDto enterRoomRequestDto
    ) {
        return roomService.addUser(roomId, enterRoomRequestDto.getUsername())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{hash}")
    public Mono<ResponseEntity<GetRoomResponseDto>> get(
            @PathVariable String hash
    ) {
        return roomService.get(hash)
                .map(ResponseEntity::ok);
    }
}
