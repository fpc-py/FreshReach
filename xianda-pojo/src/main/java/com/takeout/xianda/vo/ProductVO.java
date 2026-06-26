package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String image;
    private Integer sales;
    private String category;
}
