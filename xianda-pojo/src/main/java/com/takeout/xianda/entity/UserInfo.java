package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class UserInfo {

    private Integer id;
    @TableField("openid")
    private String openId;
    @TableField("unionid")
    private String unionId;
    private String phone;
    private String name;
    private String password;
    private String avatar;
    private  String level;
    private Integer coupons;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
