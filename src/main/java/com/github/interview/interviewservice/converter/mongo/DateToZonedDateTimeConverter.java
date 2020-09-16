package com.github.interview.interviewservice.converter.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
    private final Clock clock;

    @Override
    public ZonedDateTime convert(Date source) {
        return source.toInstant().atZone(clock.getZone());
    }
}
