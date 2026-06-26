package com.takeout.xianda.service;

import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.dto.WxLoginDTO;
import com.takeout.xianda.vo.UserLoginVO;
import com.takeout.xianda.vo.WxLoginVO;

public interface UserLoginService {
    UserLoginVO passwordLogin(LoginDTO loginDTO);

    void logout(String token);

    WxLoginVO wxLogin(WxLoginDTO wxLoginDTO);
}
