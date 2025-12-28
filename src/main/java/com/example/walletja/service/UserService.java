package com.example.walletja.service;

import com.example.walletja.entity.User;
import com.example.walletja.repository.UserRepository;
import com.example.walletja.util.PasswordUtil;
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
    public User registerUser(User user) {
        if (user.getAccountId() == null) {
            user.setAccountId(UUID.randomUUID().toString());
        }

        user.setPassword(passwordUtil.hashPassword(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> listUsers() {
        return userRepository.findAll().stream().filter(user -> !user.getIsDeleted()).toList();
    }
}