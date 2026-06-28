package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponsVO {
    private Integer id;
    private String name;
    private BigDecimal amount;
    private BigDecimal miniPrice;
    private LocalDateTime expireTime;
    private String status;
}
