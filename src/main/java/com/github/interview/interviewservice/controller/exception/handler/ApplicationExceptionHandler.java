package com.github.interview.interviewservice.controller.exception.handler;

import com.github.interview.interviewservice.configuration.properties.ExceptionProperties;
import com.github.interview.interviewservice.controller.dto.ErrorDto;
import com.github.interview.interviewservice.controller.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.ZonedDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {
    private final ExceptionProperties exceptionProperties;
    private final Clock clock;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDto> handle(ApplicationException applicationException) {
        ExceptionProperties.ErrorInfo errorInfo = exceptionProperties.getErrorMappings()
                .get(applicationException.getErrorType());

        return ResponseEntity
                .status(errorInfo.getHttpStatus())
                .body(ErrorDto.builder()
                        .context(applicationException.getContext())
                        .id(applicationException.getId())
                        .message(errorInfo.getMessage())
                        .time(ZonedDateTime.now(clock))
                        .build());
    }
}
