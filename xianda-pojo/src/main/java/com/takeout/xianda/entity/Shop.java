package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("shop_info")
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

    private Long id;
    private String name;
    private String address;
    private Integer isOpen;
    private String openTime;
    private String closeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private BigDecimal deliveryFee;
    private BigDecimal minAmount;
    private String notice;
private String type;
    @PrePersist
    protected void onCreate() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
    }

}
