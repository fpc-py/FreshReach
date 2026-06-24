package com.takeout.xianda.controller.rider;

import com.takeout.xianda.dto.GrabDTO;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.RiderService;
import com.takeout.xianda.vo.IncomeStatsVO;
import com.takeout.xianda.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "骑手管理", description = "骑手相关接口")
@RestController
@RequestMapping("/api/rider")
public class RiderController {
    @Autowired
    private RiderService riderService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        LoginVO loginVO = riderService.login(dto);
        return Result.success(loginVO);
    }


    @Operation(summary = "上线下线")
    @PutMapping("/status")
    public Result onLine(@RequestParam("status") Integer status,
                         @RequestParam("id") Long id) {
        riderService.onLine(status, id);
        return Result.success();
    }

    @Operation(summary = "可抢订单")
    @GetMapping("/available-orders")
    public Result<List<Order>> getAvailableOrders() {
        List<Order> list = riderService.getAvailableOrders();
        return Result.success(list);
    }

    @Operation(summary = "抢单")
    @PutMapping("/grab")
    public Result grabOrders(@RequestBody GrabDTO dto) {
        riderService.grabOrders(dto);
        return Result.success();
    }

    @Operation(summary = "我的订单")
    @GetMapping("/orders")
    public Result<List<Order>> getOrders(@RequestParam("id") Integer id) {
        List<Order> list = riderService.getOrders(id);
        return Result.success(list);

    }


    @Operation(summary = "确认取货")
    @PutMapping("/orders/{id}/pickup")
    public Result pickupOrders(@PathVariable String id) {
        riderService.pickupOrders(id);
        return Result.success();
    }

    @Operation(summary = "确认送达")
    @PutMapping("/orders/{id}/deliver")
    public Result deliverOrders(@PathVariable String id) {
        riderService.deliverOrders(id);
        return Result.success();
    }

    @Operation(summary = "收入统计")
    @GetMapping("/income/stats")
    public Result<IncomeStatsVO> getIncomeStats(@RequestParam("id")Integer id) {
        IncomeStatsVO list = riderService.getIncomeStats(id);
        return Result.success(list);
    }
}
