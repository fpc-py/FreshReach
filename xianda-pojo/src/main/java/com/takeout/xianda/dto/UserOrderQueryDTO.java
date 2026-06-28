package com.takeout.xianda.dto;


import lombok.Data;

@Data
public class UserOrderQueryDTO {
    private String status;
    private Integer page = 1;
    private Integer pageSize = 10;
}