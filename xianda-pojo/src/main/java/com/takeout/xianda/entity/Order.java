package com.takeout.xianda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.time.LocalDateTime;

/**
 * 订单主表实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TableName( "t_orders")
public class Order {

    @Id
    @Column(name = "id", length = 30)
    private String id; // 订单号，由后端生成，非自增

    private Long userId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Builder.Default
    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "packaging_fee", precision = 10, scale = 2)
    private BigDecimal packagingFee = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "pay_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal payAmount;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "province", length = 20)
    private String province;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "district", length = 20)
    private String district;

    @Column(name = "detail")
    private String detail;

    private Integer riderId;

    @Column(name = "rider_name", length = 50)
    private String riderName;

    @Column(name = "rider_phone", length = 20)
    private String riderPhone;

    @Column(name = "remark")
    private String remark;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    private LocalDateTime payTime;

    private String payMethod;

    private Integer payStatus;

    @PrePersist
    protected void onCreate() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
    }
}