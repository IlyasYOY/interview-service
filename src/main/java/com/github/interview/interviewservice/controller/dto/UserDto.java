package com.github.interview.interviewservice.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    String name;
    boolean creator;
}
