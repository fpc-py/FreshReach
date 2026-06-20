package com.takeout.xianda.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String username;
    private String phone;
    private String name;
    private String password;
    private Integer sex;
}
