package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FreshProductVO {

    private Long id;
    private Long shopId;
    private String name;
    private Double price;
    private String unit;
    private String image;
}
