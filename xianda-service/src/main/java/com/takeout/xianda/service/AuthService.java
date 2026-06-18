package com.takeout.xianda.service;

import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.dto.RegisterDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.vo.CaptchaVO;
import com.takeout.xianda.vo.LoginVO;
import jakarta.validation.Valid;

public interface AuthService {
    Result register(@Valid RegisterDTO dto);

    LoginVO login(LoginDTO loginDTO);

    void logout(String token);

    Result<CaptchaVO> getCaptcha();
}
