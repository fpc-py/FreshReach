package com.takeout.xianda.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TableName ("t_merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "address")
    private String address;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "open_time", length = 10)
    private String openTime;

    @Column(name = "close_time", length = 10)
    private String closeTime;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "notice", length = 500)
    private String notice;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private Double rating;

    private Integer sales;

    private String deliverTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
