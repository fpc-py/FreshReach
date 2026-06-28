package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserOrderVO {
    private Long id;
    private String status; // pending/processing/delivering/completed
    private String statusText; // 已完成
    private List<UserOrderItemVO> items;
    private BigDecimal totalPrice;
    private BigDecimal deliveryFee;
    private BigDecimal discount;
    private LocalDateTime createTime;
    private UserOrderAddressVO address;
}

