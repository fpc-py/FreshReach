package com.takeout.xianda.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    private List<OrderItemDTO> items;
    private Long addressId;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String payMethod;  // wx 或 balance
    private Long couponId;
    private String remark;
}
