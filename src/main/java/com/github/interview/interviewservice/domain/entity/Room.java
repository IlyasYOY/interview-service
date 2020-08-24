package com.github.interview.interviewservice.domain.entity;

import com.github.interview.interviewservice.domain.Hasher;
import com.github.interview.interviewservice.domain.value.Position;
import com.github.interview.interviewservice.domain.value.User;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class Room {
    private static final String ROOM_ID_USERNAME_TEMPLATE = "%s:%s";

    String id;
    long version;
    @Builder.Default
    String text = "";
    @Singular
    List<User> users;

    public static RoomBuilder forUser(String username) {
        UUID uuid = UUID.randomUUID();

        return Room.builder()
                .id(uuid.toString())
                .user(User.builder()
                        .creator(true)
                        .name(username)
                        .position(Position.builder()
                                .column(0)
                                .line(0)
                                .build())
                        .build())
                .version(0);
    }

    public String getCreatorHash(Hasher hasher) {
        return users.stream()
                .filter(User::isCreator)
                .findFirst()
                .map(user -> getUserHash(hasher, user.getName()))
                .orElse(null);
    }

    public String getUserHash(Hasher hasher, String username) {
        String roomIdAndUsername = String.format(ROOM_ID_USERNAME_TEMPLATE, id, username);
        return hasher.hash(roomIdAndUsername);
    }
}
