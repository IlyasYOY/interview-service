package com.github.interview.interviewservice.domain.value;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    String name;
    Position position;
    boolean creator;
}
