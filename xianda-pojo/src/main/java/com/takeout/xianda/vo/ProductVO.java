package com.takeout.xianda.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String image;
    private Integer sales;
    private Integer category;
    private Integer storeId;
}
