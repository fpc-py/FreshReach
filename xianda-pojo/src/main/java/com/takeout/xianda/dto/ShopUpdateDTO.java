package com.takeout.xianda.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class ShopUpdateDTO {


        /**
         * 营业开始时间
         */
        private String openTime;

        /**
         * 营业结束时间
         */
        private String closeTime;

        /**
         * 配送费
         */
        private BigDecimal deliveryFee;

        /**
         * 起送价
         */
        private BigDecimal minAmount;

        /**
         * 店铺公告
         */
        private String notice;

}
