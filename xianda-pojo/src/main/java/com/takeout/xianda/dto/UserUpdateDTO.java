package com.takeout.xianda.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {

    private String name;
    private String phone;
    private String avatar;
    private String level;
    private Integer coupons;
}
