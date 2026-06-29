package com.takeout.xianda.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.CreateOrderDTO;
import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.dto.UserOrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.vo.UserOrderVO;

import java.util.List;

public interface OrderService {
    PageResult getOrderList(OrderQueryDTO dto);

    Order getOrderDetail(String id);

    void acceptOrder(String id);

    void rejectOrder(String id, String cancelReason);

    void finishPreparing(String id);

    void agreeRefund(String id);

    void rejectRefund(String id, String cancelReason);

    Page<UserOrderVO> getUserOrderPage(UserOrderQueryDTO dto, Long userId);

    UserOrderVO getUserOrderDetail(String orderId, Long userId);

    String createOrder(CreateOrderDTO dto, Long userId);

    void cancelOrder(String orderId, Long userId, String cancelReason);
}
