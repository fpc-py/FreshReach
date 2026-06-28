package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("cart")
public class Cart {
    private Long id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer shopId;
    private String name;
    private Double price;
    private String image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
