package com.takeout.xianda.controller.user;

import com.takeout.xianda.dto.AddressDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.AddressService;
import com.takeout.xianda.vo.AddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址管理")
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Operation(summary = "获取用户收货地址列表")
    @GetMapping
    public Result<List<AddressVO>> getAddress(){
       List<AddressVO> vo = addressService.getAddress();
       return Result.success(vo);
    }

    @Operation(summary = "添加收货地址")
    @PostMapping
    public Result addAddress(@RequestBody AddressDTO dto){
        addressService.addAddress(dto);
        return Result.success();
    }
    @Operation(summary = "更新收货地址")
    @PutMapping("/{addressId}")
    public Result updateAddress(@PathVariable Long addressId,@RequestBody AddressDTO dto){
        addressService.updateAddress(addressId,dto);
        return Result.success();
    }
    @Operation(summary = "删除收货地址")
    @DeleteMapping("/{addressId}")
    public Result deleteAddress(@PathVariable Long addressId){
        addressService.deleteAddress(addressId);
        return Result.success();
    }
}
