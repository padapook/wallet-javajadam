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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SimpleResponse> handlerIllegalArgument(IllegalArgumentException ex) {
        SimpleResponse response = new SimpleResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleResponse> handlerGeneralException(Exception ex) {
        SimpleResponse response = new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL SERVER ERROR");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}