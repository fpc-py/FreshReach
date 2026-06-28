package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_coupon")
public class Coupon {
    private Integer id;
    private Integer userId;
    private Integer couponId;
    private String name;
    private BigDecimal amount;
    private BigDecimal minPrice;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private String status;
}
