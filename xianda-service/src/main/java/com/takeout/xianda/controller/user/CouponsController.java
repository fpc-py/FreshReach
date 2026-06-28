package com.takeout.xianda.controller.user;

import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.CouponsService;
import com.takeout.xianda.vo.CouponsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "优惠券")
@RestController
@RequestMapping("/api/user/coupons")
public class CouponsController {
    @Autowired
    private CouponsService couponsService;

    @Operation(summary = "获取用户优惠券")
    @GetMapping
    public Result<List<CouponsVO>> getUserCoupons(){
        List<CouponsVO> vo = couponsService.getUserCoupons();
        return Result.success(vo);
    }
}
