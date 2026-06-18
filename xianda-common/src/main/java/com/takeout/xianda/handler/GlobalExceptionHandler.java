package com.takeout.xianda.handler;

import com.takeout.xianda.exception.LoginException;

import com.takeout.xianda.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获登录自定义异常
    @ExceptionHandler(LoginException.class)
    public Result<?> handleLoginException(LoginException e) {
        // e.getCode()：4001未注册 / 4002密码错误 / 4003账号冻结
        return Result.error(e.getCode(), e.getMessage());
    }

    // 通用系统异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleOtherException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器异常");
    }
}
