package com.example.walletja.features.user.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;



public class UserDto {

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String nameTh;
        private String nameEn;
    }

    @Data
    public static class UpdateRequest {
        private String nameTh;
        private String nameEn;
    }

    @Data
    public static class Response implements Serializable {
        private String accountId;
        private String username;
        private String nameTh;
        private String nameEn;
        private LocalDateTime createdDate;
    }
}