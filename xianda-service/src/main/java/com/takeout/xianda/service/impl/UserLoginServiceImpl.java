package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.xianda.constant.JwtConstant;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.entity.UserInfo;
import com.takeout.xianda.exception.LoginException;
import com.takeout.xianda.mapper.UserInfoMapper;
import com.takeout.xianda.service.UserLoginService;
import com.takeout.xianda.utils.JwtUtil;
import com.takeout.xianda.vo.UserLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserLoginVO passwordLogin(LoginDTO loginDTO) {

        UserLoginVO vo = new UserLoginVO();
        BeanUtils.copyProperties(loginDTO,vo);

//        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<UserInfo>();
//        wrapper.eq(UserInfo::getPassword,loginDTO.getPhone());
//        UserInfo userInfo = userInfoMapper.selectOne(wrapper);

        UserInfo userInfo = userInfoMapper.selectByPhone(loginDTO.getPhone());
        System.out.println(userInfo);
        //2，BCrypt对比密码
        if(!BCrypt.checkpw(loginDTO.getPassword(),userInfo.getPassword())){
            throw new LoginException(4002,"密码错误");
        }
        String token = JwtUtil.createJwt(JwtConstant.SECRET_KEY, 60 * 60 * 1000 * 2, null);
        vo.setToken(token);
        vo.setId(userInfo.getId());
        vo.setName(userInfo.getName());
        vo.setCoupons(userInfo.getCoupons());
        vo.setAvatar(userInfo.getAvatar());
        vo.setLevel(userInfo.getLevel());


        return vo;
    }
}
