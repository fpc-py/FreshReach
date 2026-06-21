package com.takeout.xianda.dto;

import com.takeout.xianda.entity.ProductSpecs;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private Integer storeId;
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
    //口味specs数组
    private List<ProductSpecs> specs = new ArrayList<>();

}
