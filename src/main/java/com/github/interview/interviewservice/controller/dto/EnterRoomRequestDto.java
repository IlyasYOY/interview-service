package com.github.interview.interviewservice.controller.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class EnterRoomRequestDto {
    @NotBlank
    String username;
}