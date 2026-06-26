package com.takeout.xianda.service;


import com.takeout.xianda.dto.UserUpdateDTO;
import com.takeout.xianda.vo.UserLoginVO;

public interface UserManageService {
    UserLoginVO getInfo(String token);

    void updateInfo(String token,UserUpdateDTO dto);
}
