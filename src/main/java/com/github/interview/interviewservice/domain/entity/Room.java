package com.github.interview.interviewservice.domain.entity;

import com.github.interview.interviewservice.domain.Hasher;
import com.github.interview.interviewservice.domain.value.RoomUsername;
import com.github.interview.interviewservice.domain.value.User;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Value
@Builder
public class Room {
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
                        .build())
                .version(0);
    }

    public static Mono<Room> getRoomForHash(Hasher hasher, String hash, Function<String, Mono<Room>> roomFinder) {
        return RoomUsername.of(hasher, hash)
                .map(RoomUsername::getRoomId)
                .map(roomFinder)
                .orElse(Mono.empty());
    }

    public String getCreatorHash(Hasher hasher) {
        return users.stream()
                .filter(User::isCreator)
                .findFirst()
                .map(user -> getUserHash(hasher, user.getName()))
                .orElse(null);
    }

    public String getUserHash(Hasher hasher, String username) {
        RoomUsername roomIdAndUsername = RoomUsername.of(id, username);
        return roomIdAndUsername.toString(hasher);
    }

    public boolean addUser(String username) {
        boolean userExists = users.stream().anyMatch(user -> username.equalsIgnoreCase(user.getName()));

        if (userExists) {
            return false;
        }

        User user = User.builder()
                .creator(false)
                .name(username)
                .build();
        users.add(user);

        return true;
    }

    public User getUserByHash(Hasher hasher, String hash) {
        return RoomUsername.of(hasher, hash)
                .map(RoomUsername::getUsername)
                .flatMap(username -> users.stream()
                        .filter(user -> username.equalsIgnoreCase(user.getName()))
                        .findFirst())
                .orElse(null);
    }
}
