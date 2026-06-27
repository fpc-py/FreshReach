package com.takeout.xianda.controller.user;

import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.ShopService;
import com.takeout.xianda.vo.ShopVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "店铺管理")
@RestController
@RequestMapping("api/shops")
public class ShopsController {

    @Autowired
    private ShopService shopService;

    @Operation(summary = "获取店铺列表")
    @GetMapping
    public Result<PageResult> getShops(@RequestParam("type")String type,
                                       @RequestParam("page")Integer page,
                                       @RequestParam("pageSize")Integer pageSize) {
        PageResult pageResult = shopService.getShops(type,page,pageSize);
        return Result.success(pageResult);

    }

    @Operation(summary = "获取店铺详情")
    @GetMapping("/{shopId}")
    public Result<ShopVO> getDetails(@PathVariable("shopId")Integer shopId){
        ShopVO details = shopService.getDetails(shopId);
        return Result.success(details);
    }


}
