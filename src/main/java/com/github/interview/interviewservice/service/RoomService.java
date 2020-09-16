package com.github.interview.interviewservice.service;

import com.github.interview.interviewservice.controller.dto.CreateRoomResponseDto;
import com.github.interview.interviewservice.controller.dto.EnterRoomResponseDto;
import com.github.interview.interviewservice.controller.dto.GetRoomResponseDto;
import reactor.core.publisher.Mono;

public interface RoomService {
    Mono<CreateRoomResponseDto> create(String username);

    Mono<EnterRoomResponseDto> addUser(String roomId, String username);

    Mono<GetRoomResponseDto> get(String hash);
}
