package com.takeout.xianda.service;

import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.result.PageResult;

import java.util.List;

public interface OrderService {
    PageResult getOrderList(OrderQueryDTO dto);

    Order getOrderDetail(String id);
}
