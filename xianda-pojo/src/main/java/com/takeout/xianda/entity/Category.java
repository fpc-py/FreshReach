package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_category")
public class Category {
    private Long id;
    private Integer categoryId;
    private String categoryName;
    private Integer status;

    private String icon;
    private Integer sort;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
