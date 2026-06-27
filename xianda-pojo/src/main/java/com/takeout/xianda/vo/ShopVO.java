package com.takeout.xianda.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopVO {
    private Long id;
    private String name;
    private String image;
    private Double rating;
    private Integer sales;
    // 数据库字段 delivery_time，前端返回字段 time
    @JsonProperty("time")
    private String deliveryTime;
    private BigDecimal deliveryFee;
    private BigDecimal minPrice;
    private List<String> tags;
    private String type;

    private List<ProductVO> products;
}
