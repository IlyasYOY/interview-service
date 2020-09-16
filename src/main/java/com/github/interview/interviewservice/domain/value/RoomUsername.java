package com.github.interview.interviewservice.domain.value;


import com.github.interview.interviewservice.domain.Hasher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RoomUsername {
    private static final String ROOM_ID_USERNAME_SEPARATOR = ":";
    private static final String ROOM_ID_USERNAME_TEMPLATE = "%s" + ROOM_ID_USERNAME_SEPARATOR + "%s";

    String roomId;
    String username;

    public static Optional<RoomUsername> of(Hasher hasher, String hash) {
        String dehash = hasher.dehash(hash);
        String[] split = dehash.split(ROOM_ID_USERNAME_SEPARATOR);

        if (split.length != 2) {
            return Optional.empty();
        }

        return Optional.of(RoomUsername.builder()
                .roomId(split[0])
                .username(split[1])
                .build());
    }

    public String toString(Hasher hasher) {
        return hasher.hash(toString());
    }

    @Override
    public String toString() {
        return String.format(ROOM_ID_USERNAME_TEMPLATE, roomId, username);
    }
}
