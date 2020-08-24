package com.github.interview.interviewservice.controller.dto;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.Map;

@Value
@Builder
public class ErrorDto {
    String id;
    String message;
    ZonedDateTime time;
    Map<String, String> context;
}
