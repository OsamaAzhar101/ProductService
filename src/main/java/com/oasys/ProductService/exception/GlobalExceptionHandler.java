package com.oasys.ProductService.exception;

import com.oasys.common_module.clients.error.ErrorResponse;
import com.oasys.common_module.exception.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CustomException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage(), ex.getErrorCode());
        int status = ex.getStatusCode() > 0 ? ex.getStatusCode() : HttpStatus.CONFLICT.value();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        ErrorResponse body = new ErrorResponse("Internal server error", "INTERNAL_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
