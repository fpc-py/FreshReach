package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_product_sku")
public class ProductSku {

    private Long id;
    private Integer productId;
    private String skuName;
    private  Double salePrice;
    private  Double marketPrice;
    private  Integer stock;
    private  Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
