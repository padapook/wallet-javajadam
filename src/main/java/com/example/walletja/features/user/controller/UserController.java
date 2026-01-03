package com.example.walletja.features.user.controller;

import com.example.walletja.common.dto.response.DetailedResponse;
import com.example.walletja.features.user.dto.UserDto;
import com.example.walletja.features.user.entity.UserEntity;
import com.example.walletja.features.user.service.UserService;
import org.springframework.cache.CacheManager;
// import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CacheManager cacheManager;

    public UserController(UserService userService, CacheManager cacheManager) {
        this.userService = userService;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedResponse<UserDto.Response>> register(@RequestBody UserDto.RegisterRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        userEntity.setNameTh(request.getNameTh());
        userEntity.setNameEn(request.getNameEn());

        UserEntity createdUser = userService.registerUser(userEntity);

        UserDto.Response userResponse = new UserDto.Response();
        userResponse.setAccountId(createdUser.getAccountId());
        userResponse.setUsername(createdUser.getUsername());
        userResponse.setNameTh(createdUser.getNameTh());
        userResponse.setNameEn(createdUser.getNameEn());
        userResponse.setCreatedDate(createdUser.getCreatedDate());

        DetailedResponse<UserDto.Response> response = new DetailedResponse<>(
                201,
                "Register success",
                userResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // เขียนไว้อ่าน byte จาก redis
    @GetMapping("/{username}")
    public ResponseEntity<DetailedResponse<UserDto.Response>> getProfile(@PathVariable String username) {
        var cache = cacheManager.getCache("userProfile");
        String source = (cache != null && cache.get(username) != null) ? "Redis" : "PostgreSQL";

        UserDto.Response profile = userService.getUserProfile(username);

        DetailedResponse<UserDto.Response> response = new DetailedResponse<>(
                200,
                "Success",
                profile);
        response.setSource(source);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    /**
     * Endpoint สำหรับทดสอบการส่ง Message เข้า RabbitMQ
     * URL: http://localhost:8080/api/users/register-test?email=mhooham@test.com
     */
    @GetMapping("/test-message-mq")
    public ResponseEntity<String> registerTest(@RequestParam String message) {
        // เรียกใช้ Service เพื่อส่ง Message
        userService.registerUser(message);

        return ResponseEntity.ok("Producer sent message: " + message + " to RabbitMQ!");
    }
}