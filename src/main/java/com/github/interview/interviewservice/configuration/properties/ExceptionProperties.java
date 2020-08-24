package com.github.interview.interviewservice.configuration.properties;

import com.github.interview.interviewservice.controller.exception.ErrorType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Data
@Validated
@ConfigurationProperties(prefix = "exception")
public class ExceptionProperties {

    @NotNull
    private Map<@NotNull ErrorType, @NotNull ErrorInfo> errorMappings;

    @PostConstruct
    void validate() {
        List<ErrorType> errorTypesAbsent = Arrays.stream(ErrorType.values())
                .filter(not(errorMappings::containsKey))
                .collect(Collectors.toUnmodifiableList());

        if (!errorTypesAbsent.isEmpty()) {
            throw new IllegalStateException("Error types were missed: " + errorTypesAbsent.toString());
        }
    }

    @Data
    public static class ErrorInfo {
        @NotBlank
        private String message;
        @NotNull
        private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
