package com.takeout.xianda.controller.admin;

import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@Tag(name = "订单管理",description = "订单管理相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "获取订单列表")
    @GetMapping
    public Result<PageResult> getOrders(OrderQueryDTO dto){
        PageResult orders = orderService.getOrderList(dto);
        return Result.success(orders);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable String id){
        Order order = orderService.getOrderDetail(id);
        return Result.success(order);

    }



}
