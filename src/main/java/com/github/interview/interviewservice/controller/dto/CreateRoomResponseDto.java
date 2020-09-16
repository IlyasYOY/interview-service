package com.github.interview.interviewservice.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateRoomResponseDto {
    String roomId;
    String userHash;
}
