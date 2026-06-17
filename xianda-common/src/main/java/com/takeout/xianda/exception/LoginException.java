package com.takeout.xianda.exception;

public class LoginException extends RuntimeException{

    private Integer code;

    public LoginException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }
}
