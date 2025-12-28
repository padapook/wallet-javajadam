package com.example.walletja.features.user.controller;

import com.example.walletja.common.dto.ApiResponse;
import com.example.walletja.features.user.dto.UserDto;
import com.example.walletja.features.user.entity.UserEntity;
import com.example.walletja.features.user.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto.Response>> register(@RequestBody UserDto.RegisterRequest request) {
        
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

        ApiResponse<UserDto.Response> response = new ApiResponse<>(userResponse, true, 201, "Register success");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

}
