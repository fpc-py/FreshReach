package com.takeout.xianda.service;

import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.result.PageResult;

import java.util.List;

public interface OrderService {
    PageResult getOrderList(OrderQueryDTO dto);

    Order getOrderDetail(String id);

    void acceptOrder(String id);

    void rejectOrder(String id, String cancelReason);

    void finishPreparing(String id);

    void agreeRefund(String id);

    void rejectRefund(String id, String cancelReason);
}
