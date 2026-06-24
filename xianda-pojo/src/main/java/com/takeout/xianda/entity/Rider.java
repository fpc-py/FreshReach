package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_rider")
public class Rider {

private Long id;
private String riderName;
private String phone;
private String password;
private Integer status;
private BigDecimal score;
private LocalDateTime createTime;
private LocalDateTime updateTime;

}
