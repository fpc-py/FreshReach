package com.takeout.xianda.controller.user;

import com.takeout.xianda.dto.UserUpdateDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.UserManageService;
import com.takeout.xianda.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("api/user")
public class UserManageController {
    @Autowired
    private UserManageService userManageService;

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public Result<UserLoginVO> info(@RequestHeader("Authorization")  String token){
        UserLoginVO vo = userManageService.getInfo(token);
        return Result.success(vo);
    }

    @Operation(summary = "修改用户信息")
    @PutMapping("/info")
    public Result updateInfo(@RequestHeader("Authorization")  String token, @RequestBody UserUpdateDTO dto){
        userManageService.updateInfo(token,dto);
        return Result.success();

    }

}
