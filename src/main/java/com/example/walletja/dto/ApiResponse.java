package com.example.walletja.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(T data, boolean success, int code, String message) {
        this.data = data;
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}
