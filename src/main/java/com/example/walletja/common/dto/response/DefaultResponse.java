package com.example.walletja.common.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class DefaultResponse {
    protected boolean success;
    protected int code;
    protected String message;
    protected String source;
}
