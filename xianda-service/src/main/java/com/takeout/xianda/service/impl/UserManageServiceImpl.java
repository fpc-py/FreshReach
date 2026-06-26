package com.takeout.xianda.service.impl;

import com.takeout.xianda.dto.UserUpdateDTO;
import com.takeout.xianda.entity.UserInfo;
import com.takeout.xianda.mapper.UserManageMapper;
import com.takeout.xianda.service.UserManageService;
import com.takeout.xianda.utils.JwtUtil;
import com.takeout.xianda.vo.UserLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private UserManageMapper userManageMapper;

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public UserLoginVO getInfo(String token) {
        Long userId = jwtUtil.getUserId(token);
        UserInfo info = userManageMapper.selectById(userId);
        UserLoginVO vo = new UserLoginVO();
        BeanUtils.copyProperties(info, vo);
        return vo;
    }

    @Override
    public void updateInfo(String token,UserUpdateDTO dto) {
        Long userId = jwtUtil.getUserId(token);
        UserInfo info = userManageMapper.selectById(userId);
        BeanUtils.copyProperties(dto, info);
        userManageMapper.updateById(info);
    }
}
