package com.github.interview.interviewservice.controller.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class GetRoomResponseDto {
    String text;
    @Builder.Default
    List<UserDto> users = Collections.emptyList();
    UserDto you;
}
