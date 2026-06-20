package com.takeout.xianda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "员工分页查询参数")
public class EmployeePageQueryDTO {

    @Schema(description = "页码",example = "1")
    private Integer pageNum = 1;
    @Schema(description = "每页数量",example = "10")
    private Integer pageSize = 10;

}
