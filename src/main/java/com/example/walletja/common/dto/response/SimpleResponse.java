package com.example.walletja.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
// @AllArgsConstructor
public class SimpleResponse extends DefaultResponse {
    public SimpleResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.success = (code >= 200 && code < 300);
    }

    public SimpleResponse(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }
}