package com.takeout.xianda.vo;

import lombok.Data;

@Data
public class WxLoginVO {
    private String token;
    private Integer userId;
    private String name;
    private String avatar;
}
