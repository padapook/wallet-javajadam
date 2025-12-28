package com.example.walletja.common.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetailedResponse<T> extends DefaultResponse {
    private T data;

    public DetailedResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = (code >= 200 && code < 300);
    }
}