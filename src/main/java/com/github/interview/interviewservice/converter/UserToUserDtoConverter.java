package com.github.interview.interviewservice.converter;

import com.github.interview.interviewservice.controller.dto.UserDto;
import com.github.interview.interviewservice.domain.value.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User source) {
        if (source == null) {
            return null;
        }

        return UserDto.builder()
                .creator(source.isCreator())
                .name(source.getName())
                .build();
    }
}
