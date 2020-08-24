package com.github.interview.interviewservice.repository;

import com.github.interview.interviewservice.model.RoomModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RoomRepository extends ReactiveMongoRepository<RoomModel, String> {
}
