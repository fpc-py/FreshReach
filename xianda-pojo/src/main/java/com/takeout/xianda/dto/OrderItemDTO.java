package com.takeout.xianda.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String image;
}

