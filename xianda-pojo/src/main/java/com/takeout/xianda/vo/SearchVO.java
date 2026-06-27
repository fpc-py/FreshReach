package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SearchVO {

    private Long id;
    private String name;
    private BigDecimal price;
    private String image;

}
