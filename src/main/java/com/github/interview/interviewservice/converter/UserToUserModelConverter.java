package com.github.interview.interviewservice.converter;

import com.github.interview.interviewservice.domain.value.Position;
import com.github.interview.interviewservice.domain.value.User;
import com.github.interview.interviewservice.model.PositionModel;
import com.github.interview.interviewservice.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserToUserModelConverter implements Converter<User, UserModel> {
    private final Converter<Position, PositionModel> positionConverter;

    @Override
    public UserModel convert(User source) {
        return UserModel.builder()
                .name(source.getName())
                .position(positionConverter.convert(source.getPosition()))
                .build();
    }
}
