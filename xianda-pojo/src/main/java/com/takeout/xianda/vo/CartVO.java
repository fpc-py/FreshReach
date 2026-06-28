package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVO {
    private Long id;
    private Integer productId;
    private String name;
    private Double price;
    private Integer quantity;
    private String image;
}
