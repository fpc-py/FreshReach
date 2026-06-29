package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserOrderVO {
    private String id;          // 订单ID（订单号字符串）
    private String orderNo;
    private String status; // pending/processing/delivering/completed
    private String statusText; // 已完成
    private Long shopId;
    private String shopName;
    private List<UserOrderItemVO> items;
    private BigDecimal goodsAmount;
    private BigDecimal payPrice;
    private BigDecimal totalPrice;
    private BigDecimal deliveryFee;
    private BigDecimal discount;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private UserOrderAddressVO address;
}

