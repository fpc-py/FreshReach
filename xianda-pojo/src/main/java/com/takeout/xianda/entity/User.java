package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@TableName("sys_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String username;
    private String phone;
    private Integer role;

    private Integer sex;//0-女  1-男


    //数据库加密存储，不返回给前端
    @JsonIgnore
    private String password;
    private Integer status;   //0-禁用，1-正常
    private LocalDateTime createTime;
}
