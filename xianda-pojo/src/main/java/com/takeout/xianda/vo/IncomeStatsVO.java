package com.takeout.xianda.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncomeStatsVO {
    // 今日完成订单数量
    private Long todayOrderCount;
    // 今日总收入
    private BigDecimal todayIncome;
    // 累计总收入
    private BigDecimal totalIncome;
}
