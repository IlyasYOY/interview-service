package com.github.interview.interviewservice.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomModel {
    @Id
    String id;
    List<UserModel> users;
    String text;
    Long version;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;
}
