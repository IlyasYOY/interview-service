package com.github.interview.interviewservice.domain;

public interface Hasher {
    String hash(String string);

    String dehash(String string);
}
