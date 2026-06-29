package com.takeout.xianda.controller.user;




import com.takeout.xianda.dto.CreateOrderDTO;
import com.takeout.xianda.utils.JwtUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.UserOrderQueryDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.OrderService;
import com.takeout.xianda.vo.UserOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "订单管理")
@RestController
@RequestMapping("api/user/orders")
public class UserOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
private JwtUtil jwtUtil;

    @Operation(summary = "获取用户订单列表")
    @GetMapping
    public Result<Page<UserOrderVO>> getUserOrders(
            UserOrderQueryDTO dto,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (token == null || token.trim().isEmpty()){
            return Result.error(401, "请先登录");
        }
        try {
            Long userId = jwtUtil.getUserId(token);
            Page<UserOrderVO> page = orderService.getUserOrderPage(dto, userId);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @Operation(summary = "获取用户订单详情")
    @GetMapping("/{orderId}")
    public Result<UserOrderVO> getOrderDetail(
            @PathVariable String orderId,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (token == null || token.trim().isEmpty()){
            return Result.error(401, "请先登录");
        }
        try {
            Long userId = jwtUtil.getUserId(token);
            UserOrderVO order = orderService.getUserOrderDetail(orderId, userId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<Map<String, Object>> createOrder(
            @RequestBody CreateOrderDTO dto,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (token == null || token.trim().isEmpty()){
            return Result.error(401, "请先登录");
        }
        try {
            Long userId = jwtUtil.getUserId(token);
            String orderId = orderService.createOrder(dto, userId);

            // 返回订单基本信息
            Map<String, Object> result = new HashMap<>();
            result.put("id", orderId);
            result.put("orderNo", orderId);
            result.put("status", "pending");
            result.put("statusText", "待接单");
            result.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{orderId}/cancel")
    public Result<Void> cancelOrder(
            @PathVariable String orderId,
            @RequestParam(required = false) String cancelReason,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (token == null || token.trim().isEmpty()){
            return Result.error(401, "请先登录");
        }
        try {
            Long userId = jwtUtil.getUserId(token);
            orderService.cancelOrder(orderId, userId, cancelReason);
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}
