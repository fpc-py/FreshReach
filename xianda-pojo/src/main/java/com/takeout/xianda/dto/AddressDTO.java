package com.takeout.xianda.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String name;
    private String phone;
    private String detail;
    private Boolean isDefault;
}
