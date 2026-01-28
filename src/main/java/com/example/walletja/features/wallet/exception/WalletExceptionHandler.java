package com.example.walletja.features.wallet.exception;

import com.example.walletja.common.dto.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class WalletExceptionHandler {
    @ExceptionHandler(WalletException.class)
    public ResponseEntity<SimpleResponse> handleWalletException(WalletException ex) {
        SimpleResponse response = new SimpleResponse(400, ex.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleResponse> handleGeneralException(Exception ex) {
        SimpleResponse response = new SimpleResponse(500, "เกิดข้อผิดพลาดภายในระบบ: " + ex.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .get(0)
            .getDefaultMessage();
        SimpleResponse response = new SimpleResponse(400, errorMessage, false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
