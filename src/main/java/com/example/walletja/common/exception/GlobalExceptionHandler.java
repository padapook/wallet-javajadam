package com.example.walletja.common.exception;

// import com.example.walletja.common.dto.ApiResponse;
import com.example.walletja.common.dto.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<SimpleResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        SimpleResponse response = new SimpleResponse(409, ex.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}