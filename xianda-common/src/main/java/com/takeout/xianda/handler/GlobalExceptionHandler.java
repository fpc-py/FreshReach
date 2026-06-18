package com.takeout.xianda.handler;

import com.takeout.xianda.exception.LoginException;

import com.takeout.xianda.result.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获登录自定义异常
    @ExceptionHandler(LoginException.class)
    public Result<?> handleLoginException(LoginException e) {
        // e.getCode()：4001未注册 / 4002密码错误 / 4003账号冻结
        return Result.error(e.getCode(), e.getMessage());
    }
    // 处理参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // 返回第一个错误信息
        String message = errors.values().stream().findFirst().orElse("参数验证失败");
        return Result.error(400, message);
    }
    // 通用系统异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleOtherException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器异常");
    }
}
