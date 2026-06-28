package com.takeout.xianda.vo;

import lombok.Data;

@Data
public class AddressVO {
    private Long id;
    private String name;
    private String phone;
    private String detail;
    private Boolean isDefault;
}
