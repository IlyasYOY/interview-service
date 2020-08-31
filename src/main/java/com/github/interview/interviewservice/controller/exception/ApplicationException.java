package com.github.interview.interviewservice.controller.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {
    String id;
    ErrorType errorType;
    Map<String, String> context;

    public static ApplicationException of(ErrorType errorType) {
        return of(errorType, Collections.emptyMap());
    }

    public static ApplicationException of(ErrorType errorType, Map<String, String> context) {
        UUID uuid = UUID.randomUUID();
        return new ApplicationException(uuid.toString(), errorType, context);
    }
}
