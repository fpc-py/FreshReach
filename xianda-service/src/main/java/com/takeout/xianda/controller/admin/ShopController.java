package com.takeout.xianda.controller.admin;


import com.takeout.xianda.dto.ShopStatusDTO;
import com.takeout.xianda.dto.ShopUpdateDTO;
import com.takeout.xianda.entity.Shop;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "店铺管理",description = "店铺管理相关接口")
@RestController
@RequestMapping("api/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Operation(summary = "获取店铺信息")
    @GetMapping("/info")
    public Result<List<Shop>> getShopInfo(){
        List<Shop> list = shopService.getShopInfo();
        return Result.success(list);
    }

    @Operation(summary = "更新店铺信息")
    @PutMapping("/info")
    public Result updateShopInfo(@RequestBody ShopUpdateDTO dto){
        shopService.updateShopInfo(dto);
        return Result.success();
    }

    @Operation(summary = "更新营业状态")
    @PutMapping("/status")
    public Result updateShopStatus(@RequestBody ShopStatusDTO dto){
        shopService.updateShopStatus(dto);
        return Result.success();
    }

}
