package com.takeout.xianda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品分页查询参数")
public class ProductPageQueryDTO {
    @Schema(description = "页码",example = "1")
    private Integer pageNum = 1;
    @Schema(description = "每页数量",example = "10")
    private Integer pageSize = 10;

    @Schema(description = "商品名称")
    private String productName;

}
