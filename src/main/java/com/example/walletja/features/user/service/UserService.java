package com.example.walletja.features.user.service;

import com.example.walletja.common.util.PasswordUtil;
import com.example.walletja.features.user.entity.UserEntity;
import com.example.walletja.features.user.repository.UserRepository;

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
        if (user.getAccountId() == null) {
            user.setAccountId(UUID.randomUUID().toString());
        }

        user.setPassword(passwordUtil.hashPassword(user.getPassword()));

        return userRepository.save(user);
    }

    public List<UserEntity> listUsers() {
        return userRepository.findAll().stream().filter(user -> !user.getIsDeleted()).toList();
    }
}