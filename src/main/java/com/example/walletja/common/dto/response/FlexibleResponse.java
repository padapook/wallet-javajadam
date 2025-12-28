package com.example.walletja.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class FlexibleResponse<T> extends DefaultResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL) 
    private T data;

    public FlexibleResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = (code >= 200 && code < 300);
    }
}