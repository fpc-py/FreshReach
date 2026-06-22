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
import java.util.Map;

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

    @Operation(summary = "接单")
    @PutMapping("/{id}/accept")
    public Result accept(@PathVariable String id){
        orderService.acceptOrder(id);
        return Result.success();
    }

    @Operation(summary = "拒单")
    @PutMapping("/{id}/reject")
    public Result reject(
            @PathVariable String id,
            @RequestBody Map<String,String> param
    ){
        String cancelReason = param.get("cancelReason");
        orderService.rejectOrder(id, cancelReason);
        return Result.success();
    }

    @Operation(summary = "完成备餐")
    @PutMapping("/{id}/prepare-done")
    public Result prepareDone(@PathVariable String id){
        orderService.finishPreparing(id);
        return Result.success();
    }

    @Operation(summary = "同意退款")
    @PutMapping("/{id}/refund/agree")
    public Result refundAgree(@PathVariable String id){
        orderService.agreeRefund(id);
        return Result.success();
    }

    @Operation(summary = "拒接退款")
    @PutMapping("/{id}/refund/reject")
    public Result refundReject(@PathVariable String id,
                               @RequestBody Map<String,String> param){
        String cancelReason =  param.get("cancelReason");
        orderService.rejectRefund(id,cancelReason);
        return Result.success();
    }



}
