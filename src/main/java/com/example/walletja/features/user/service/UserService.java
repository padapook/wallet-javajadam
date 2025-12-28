package com.example.walletja.features.user.service;

// import com.example.walletja.common.dto.ApiResponse;
import com.example.walletja.common.exception.UserAlreadyExistsException;
import com.example.walletja.common.util.PasswordUtil;
import com.example.walletja.features.user.entity.UserEntity;
import com.example.walletja.features.user.repository.UserRepository;
import com.example.walletja.features.user.dto.UserDto;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Transactional
    public UserEntity registerUser(UserEntity user) {
        userRepository.findUserByUsername(user.getUsername())
            .ifPresent(u -> { 
                throw new UserAlreadyExistsException("Username already exists"); 
            });

        if (user.getAccountId() == null) {
            user.setAccountId(UUID.randomUUID().toString());
        }
        user.setPassword(passwordUtil.hashPassword(user.getPassword()));
        
        return userRepository.save(user);
    }

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