package com.github.interview.interviewservice.service;

import com.github.interview.interviewservice.controller.dto.RoomCreatedDto;
import reactor.core.publisher.Mono;

public interface RoomService {
    Mono<RoomCreatedDto> create(String username);
}
