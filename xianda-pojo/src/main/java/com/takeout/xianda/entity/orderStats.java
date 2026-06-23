package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@TableName("order_stats")
public class orderStats {
    private Long id;
    private Long shopId;
    private LocalDate date;
    private Integer totalOrders;
    private BigDecimal totalAmount;
    private Integer completedOrders;
    private Integer cancelledOrders;
    private LocalDateTime createdAt;
}
