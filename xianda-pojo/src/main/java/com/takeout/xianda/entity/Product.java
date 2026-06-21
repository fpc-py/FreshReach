package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;

@Data
@TableName("t_product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Long id;

    private Integer storeId;

    private Integer categoryId;

    private String productName;

    private String coverImage;

    private String description;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
