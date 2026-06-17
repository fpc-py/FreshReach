package com.takeout.xianda.service;

import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.vo.LoginVO;

public interface AuthService {
    LoginVO login(LoginDTO loginDTO);

//    void logout();
}
