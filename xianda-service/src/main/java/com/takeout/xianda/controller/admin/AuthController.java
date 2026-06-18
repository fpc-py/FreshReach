package com.takeout.xianda.controller.admin;

import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.dto.RegisterDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.AuthService;
import com.takeout.xianda.vo.CaptchaVO;
import com.takeout.xianda.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "用户登录、注册等认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthService userService;

    @Operation(summary = "用户登录",description = "根据手机号和密码登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated@RequestBody LoginDTO loginDTO){
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    @Operation(summary = "用户注册",description = "根据手机号和密码注册")
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDTO dto){
        return userService.register(dto);
    }

    @Operation(summary = "用户登出",description = "登出系统")
    @PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }

    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha(){
        return userService.getCaptcha();
    }
}
