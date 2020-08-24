package com.github.interview.interviewservice.service;

import com.github.interview.interviewservice.domain.Hasher;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

@Component
public class Base64Hasher implements Hasher {
    @Override
    @SneakyThrows
    public String hash(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return Base64Utils.encodeToString(bytes);
    }

    @Override
    public String dehash(String string) {
        byte[] bytes = Base64Utils.decodeFromString(string);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
