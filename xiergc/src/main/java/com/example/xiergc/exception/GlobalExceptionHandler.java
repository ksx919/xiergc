package com.example.xiergc.exception;

import com.example.xiergc.entity.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Result<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        // 拼接所有校验错误信息
        for (FieldError error : fieldErrors) {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        }

        // 返回一个 Result 对象
        return ResponseEntity.badRequest().body(Result.error(errorMessage.toString()));
    }

    // 捕获其他异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> handleException(Exception ex) {
        return new ResponseEntity<>(Result.error("系统异常：" + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

