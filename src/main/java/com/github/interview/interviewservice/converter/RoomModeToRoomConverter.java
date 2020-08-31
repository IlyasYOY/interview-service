package com.github.interview.interviewservice.converter;

import com.github.interview.interviewservice.domain.entity.Room;
import com.github.interview.interviewservice.domain.value.User;
import com.github.interview.interviewservice.model.RoomModel;
import com.github.interview.interviewservice.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomModeToRoomConverter implements Converter<RoomModel, Room> {
    private final Converter<UserModel, User> userConverter;

    @Override
    public Room convert(RoomModel source) {
        List<User> users = source.getUsers().stream()
                .map(userConverter::convert)
                .collect(Collectors.toList());

        return Room.builder()
                .users(users)
                .text(source.getText())
                .version(source.getVersion())
                .id(source.getId())
                .build();
    }
}
