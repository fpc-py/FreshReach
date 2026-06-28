package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserOrderItemVO {
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}