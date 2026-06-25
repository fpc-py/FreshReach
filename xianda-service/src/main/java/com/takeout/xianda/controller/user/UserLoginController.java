package com.takeout.xianda.controller.user;

import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.UserLoginService;
import com.takeout.xianda.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户登录")
@RestController
@RequestMapping("/api/user")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @Operation(summary = "手机号登录")
    @PostMapping("/password-login")
    public Result<UserLoginVO> passwordLogin(@RequestBody LoginDTO loginDTO){

        UserLoginVO userInfo = userLoginService.passwordLogin(loginDTO);
        return Result.success(userInfo);
    }
}
