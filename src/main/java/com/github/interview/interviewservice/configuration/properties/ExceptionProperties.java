package com.github.interview.interviewservice.configuration.properties;

import com.github.interview.interviewservice.controller.exception.ErrorType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "exception")
public class ExceptionProperties {

    @NotNull
    Map<@NotNull ErrorType, @NotNull ErrorInfo> errorMappings;

    @NotNull
    HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    @NotBlank
    String defaultMessage = "Error happened during request";

    @PostConstruct
    public void show() {
        log.info("Error mappings were consumed: {}", this);
    }


    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ErrorInfo {
        @NotBlank
        String message;
        HttpStatus httpStatus;
    }
}
