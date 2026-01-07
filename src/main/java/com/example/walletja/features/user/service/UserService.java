package com.example.walletja.features.user.service;

// import com.example.walletja.common.dto.ApiResponse;

import com.example.walletja.common.entity.OutboxEntity;
import com.example.walletja.common.repository.OutboxRepository;
import com.example.walletja.common.exception.UserAlreadyExistsException;
import com.example.walletja.common.util.PasswordUtil;
import com.example.walletja.features.user.entity.UserEntity;
import com.example.walletja.features.user.repository.UserRepository;
import com.example.walletja.features.user.config.UserRabbitConfig;
import com.example.walletja.features.user.dto.UserDto;
import com.example.walletja.features.user.dto.UserEventDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    // private final RabbitTemplate rabbitTemplate;

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;


    // public UserService(UserRepository userRepository, PasswordUtil passwordUtil, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        // this.userRepository = userRepository;
        // this.passwordUtil = passwordUtil;
        // this.outboxRepository = outboxRepository;
        // this.objectMapper = objectMapper;
        // this.rabbitTemplate = rabbitTemplate;
    // }

    @Transactional
    public UserEntity registerUser(UserEntity user) {
        // user ต้องเป็น alphanumeric
        if (!user.getUsername().matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Username must be alphanumeric only");
        }

        // check user dup
        userRepository.findUserByUsername(user.getUsername())
            .ifPresent(u -> { 
                throw new UserAlreadyExistsException("Username already exists"); 
            });

        if (user.getAccountId() == null) {
            user.setAccountId(UUID.randomUUID().toString());
        }
        user.setPassword(passwordUtil.hashPassword(user.getPassword()));

        UserEntity savedUserData = userRepository.save(user);

        sendToOutBox(savedUserData);
        
        // return userRepository.save(user);
        return savedUserData;
    }

    public void sendToOutBox(UserEntity user) {
        try {
            // entity -> dto
            UserEventDto eventDto = new UserEventDto(user.getUsername());

            // dto -> json (มันคือ JSON.stringify(eventDto))
            String payload = objectMapper.writeValueAsString(eventDto);

            // @Id
            // @GeneratedValue(strategy = GenerationType.IDENTITY)
            // private Long id;

            // @Column(nullable = false)
            // private String aggregateId;

            // @Column(nullable = false)
            // private String aggregateType;

            // @Column(columnDefinition = "text", nullable = false)
            // private String payload;

            // @Column(nullable = false)
            // private String exchange;

            // @Column(nullable = false)
            // private String routingKey;

            // private boolean processed = false;

            // private LocalDateTime createdDate = LocalDateTime.now();

            OutboxEntity outbox = new OutboxEntity();
            outbox.setAggregateId((user.getUsername()));
            outbox.setAggregateType("USER_REGISTRATION");
            outbox.setPayload(payload);
            outbox.setExchange(UserRabbitConfig.EXCHANGE);
            outbox.setRoutingKey(UserRabbitConfig.KEY_USER_REGISTRATION);

            outboxRepository.save(outbox);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert event to json", e);
        }
    }

    // ย้าย rabbittemplate ไปไว้ที่ outbox worker แทน
    // public void sendUserRegistrationMessage(UserEntity user) {
    //     // entity -> dto
    //     UserEventDto event = new UserEventDto(user.getUsername());

    //     rabbitTemplate.convertAndSend(
    //         com.example.walletja.features.user.config.UserRabbitConfig.EXCHANGE,
    //         com.example.walletja.features.user.config.UserRabbitConfig.KEY_USER_REGISTRATION,
    //         // user
    //         event
    //     );
    // }

    public List<UserEntity> listUsers() {
        return userRepository.findAll().stream().filter(user -> !user.getIsDeleted()).toList();
    }

    @Transactional
    @Cacheable(value = "userProfile", key = "#username")
    public UserDto.Response getUserProfile(String username) {
        UserEntity user = userRepository.findActiveUserByUsername(username)
                .filter(u -> !u.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        UserDto.Response response = new UserDto.Response();
        response.setAccountId(user.getAccountId());
        response.setUsername(user.getUsername());
        response.setNameTh(user.getNameTh());
        response.setNameEn(user.getNameEn());
        response.setCreatedDate(user.getCreatedDate());

        return response;
    }
}