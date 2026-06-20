package com.takeout.xianda.vo;

import lombok.Data;


@Data
public class LoginVO {

    private String token;
    private Long userId;
    private String phone;
    private String username;
    private Integer role;


}
