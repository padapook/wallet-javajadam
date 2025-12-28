package com.example.walletja.common.dto.response;

public class SimpleResponse extends DefaultResponse {
    public SimpleResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.success = (code >= 200 && code < 300);
    }
}