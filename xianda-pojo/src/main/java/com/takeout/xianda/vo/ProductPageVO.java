package com.takeout.xianda.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductPageVO {

    private Long id;
    private Long storeId;
    private Long categoryId;
    private String productName;
    private String coverImage;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    //sku关联字段
    private BigDecimal salePrice;
    private BigDecimal marketPrice;
    private Integer stock;
}
