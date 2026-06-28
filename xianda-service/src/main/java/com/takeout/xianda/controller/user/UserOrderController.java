package com.takeout.xianda.controller.user;




import com.takeout.xianda.utils.JwtUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.UserOrderQueryDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.OrderService;
import com.takeout.xianda.vo.UserOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "订单管理")
@RestController
@RequestMapping("api/user")
public class UserOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
private JwtUtil jwtUtil;

    @Operation(summary = "获取用户订单列表")
    @GetMapping("/orders")
    public Result<Page<UserOrderVO>> getUserOrders(
            UserOrderQueryDTO dto,
            @RequestHeader(required = false) String token
    ){
        // 从token解析当前登录用户ID
        Long userId = jwtUtil.getUserId(token);
        Page<UserOrderVO> page = orderService.getUserOrderPage(dto, userId);
        return Result.success(page);
    }

}
