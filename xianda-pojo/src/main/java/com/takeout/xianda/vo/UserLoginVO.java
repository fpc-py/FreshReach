package com.takeout.xianda.vo;

import lombok.Data;

@Data
public class UserLoginVO {

    private String token;
    private Integer id;
    private String name;
    private String phone;
    private String avatar;
    private String level;
    private Integer coupons;

}
