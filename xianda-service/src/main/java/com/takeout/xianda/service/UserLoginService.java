package com.takeout.xianda.service;

import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.vo.UserLoginVO;

public interface UserLoginService {
    UserLoginVO passwordLogin(LoginDTO loginDTO);

    void logout(String token);
}
