package com.github.interview.interviewservice.converter;

import com.github.interview.interviewservice.domain.entity.Room;
import com.github.interview.interviewservice.domain.value.User;
import com.github.interview.interviewservice.model.RoomModel;
import com.github.interview.interviewservice.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomToRoomModelConverter implements Converter<Room, RoomModel> {
    private final Converter<User, UserModel> userConverter;

    @Override
    public RoomModel convert(Room source) {
        return RoomModel.builder()
                .id(source.getId())
                .text(source.getText())
                .version(source.getVersion())
                .users(source.getUsers().stream()
                        .map(userConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
