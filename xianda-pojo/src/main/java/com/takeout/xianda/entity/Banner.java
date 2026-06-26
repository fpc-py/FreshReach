package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("banner")
public class Banner {

    private Long id;
    private String image;
    private String title;
}
