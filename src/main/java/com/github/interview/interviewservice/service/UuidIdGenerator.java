package com.github.interview.interviewservice.service;

import com.github.interview.interviewservice.domain.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdGenerator implements IdGenerator {
    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
