package com.github.interview.interviewservice.controller;

import com.github.interview.interviewservice.controller.dto.RoomCreatedDto;
import com.github.interview.interviewservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(RoomController.ROOT_URL)
public class RoomController {

    public static final String ROOT_URL = "/room";
    public static final String USER_ID = "USER-ID";

    private final RoomService roomService;

    @PostMapping
    public Mono<ResponseEntity<RoomCreatedDto>> create(
            @RequestHeader(USER_ID) String username
    ) {
        Mono<RoomCreatedDto> roomCreatedDtoMono = roomService.create(username);
        return roomCreatedDtoMono
                .map(ResponseEntity::ok);
    }
}
