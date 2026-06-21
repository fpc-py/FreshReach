package com.takeout.xianda.entity;

import lombok.Data;

@Data
public class ProductSpecs {
    private Long id;
    private Long productId;
    private String specsName;
    private String specsOptions;
}
