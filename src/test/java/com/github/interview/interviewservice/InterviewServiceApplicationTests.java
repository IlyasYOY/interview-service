package com.github.interview.interviewservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.interview.interviewservice.controller.dto.CreateRoomRequestDto;
import com.github.interview.interviewservice.controller.dto.CreateRoomResponseDto;
import com.github.interview.interviewservice.controller.dto.EnterRoomRequestDto;
import com.github.interview.interviewservice.domain.IdGenerator;
import com.github.interview.interviewservice.initializer.MongoInitializer;
import com.github.interview.interviewservice.model.RoomModel;
import com.github.interview.interviewservice.model.UserModel;
import com.github.interview.interviewservice.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = MongoInitializer.class)
class InterviewServiceApplicationTests {
    public static final String USERNAME = "ilyasyoy";

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IdGenerator idGenerator;
    @Autowired
    private List<ReactiveMongoRepository> reactiveMongoRepositoryList;
    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    public void setUp() {
        reactiveMongoRepositoryList.forEach(reactiveMongoRepository -> {
            reactiveMongoRepository.deleteAll().block();
        });
    }


    @Test
    @DisplayName("Test room creation")
    void testCreateRoom() throws JsonProcessingException {
        String id = UUID.randomUUID().toString();
        Mockito.when(idGenerator.get()).thenReturn(id);

        String s = id + ":" + USERNAME;
        CreateRoomResponseDto build = CreateRoomResponseDto.builder()
                .roomId(id)
                .userHash(Base64Utils.encodeToString(s.getBytes()))
                .build();

        webTestClient.method(HttpMethod.POST)
                .uri("/rooms")
                .header("Content-Type", "application/json")
                .bodyValue(CreateRoomRequestDto.builder()
                        .username(USERNAME)
                        .build())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .json(objectMapper.writeValueAsString(build));
    }

    @Test
    @DisplayName("Test that we successfully load room info")
    void testGetRoom() {
        RoomModel roomModel = roomRepository.save(RoomModel.builder()
                .users(List.of(
                        UserModel.builder()
                                .creator(true)
                                .name(USERNAME)
                                .createdAt(LocalDateTime.now())
                                .build()))
                .version(0L)
                .text("Empty")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build())
                .block();
        String hash = Base64Utils.encodeToString(String.format("%s:%s", roomModel.getId(), USERNAME).getBytes());

        webTestClient.method(HttpMethod.GET)
                .uri("/rooms/" + hash)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBody()
                .jsonPath("$.text").isEqualTo("Empty")
                .jsonPath("$.you").isNotEmpty()
                .jsonPath("$.users").isArray()
                .jsonPath("$.users").isNotEmpty();
    }

    @Test
    @DisplayName("Test that we successfully enter a room")
    void testEnterRoom() {
        RoomModel roomModel = roomRepository.save(RoomModel.builder()
                .users(List.of(
                        UserModel.builder()
                                .creator(true)
                                .name(USERNAME)
                                .createdAt(LocalDateTime.now())
                                .build()))
                .version(0L)
                .text("Empty")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build())
                .block();

        webTestClient.method(HttpMethod.POST)
                .uri("/rooms/" + roomModel.getId() + "/enter")
                .bodyValue(EnterRoomRequestDto.builder()
                        .username("newbie")
                        .build())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBody()
                .jsonPath("$.roomId").isEqualTo(roomModel.getId())
                .jsonPath("$.userHash").isNotEmpty()
                .jsonPath("$.userHash").isEqualTo(Base64Utils.encodeToString(String.format("%s:%s", roomModel.getId(), "newbie").getBytes()));
    }


    @Test
    @DisplayName("Test that we successfully enter a room twice")
    void testEnterRoomError() {
        RoomModel roomModel = roomRepository.save(RoomModel.builder()
                .users(List.of(
                        UserModel.builder()
                                .creator(true)
                                .name(USERNAME)
                                .createdAt(LocalDateTime.now())
                                .build()))
                .version(0L)
                .text("Empty")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build())
                .block();

        webTestClient.method(HttpMethod.POST)
                .uri("/rooms/" + roomModel.getId() + "/enter")
                .bodyValue(EnterRoomRequestDto.builder()
                        .username(USERNAME)
                        .build())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.time").exists()
                .jsonPath("$.id").exists()
                .jsonPath("$.message").isEqualTo("Error happened during request")
                .jsonPath("$.context").exists()
                .jsonPath("$.context").isMap()
                .jsonPath("$.context.room").isEqualTo(roomModel.getId())
                .jsonPath("$.context.user").isEqualTo("ilyasyoy");
    }

}
