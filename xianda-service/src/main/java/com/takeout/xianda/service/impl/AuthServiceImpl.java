package com.takeout.xianda.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.exception.LoginException;

import com.takeout.xianda.mapper.UserMapper;
import com.takeout.xianda.service.AuthService;
import com.takeout.xianda.utils.JwtUtil;
import com.takeout.xianda.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;



@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginVO login(LoginDTO dto) {

        //1,根据手机号查询 用户
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getPhone, dto.getPhone()));
        if (user == null) {
            throw new LoginException(4001,"该手机号未注册");
        }

//        //2，BCrypt对比密码
//        if(!BCrypt.checkpw(dto.getPassword(),user.getPassword())){
//            throw new LoginException(4002,"密码错误");
//        }

        //3，校验账号状态
        if(user.getStatus() == 0){
            throw new LoginException(4003,"账号被冻结，请联系管理员");
        }

        //4，生成JWT token
        String token = JwtUtil.createJwt("xiandaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 60 * 60 * 1000 * 2, null);
        //5,封装返回VO
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setUsername(user.getUsername());
        return vo;


    }

    //token失效
//    @Override
//    public void logout(String token) {
//        String realToken = token.replace("Bearer ", "");
//       long expire = JwtUtil.getExpireTime(realToken)-System.currentTimeMillis();
//       redisTemplate.opsForValue().set(
//               "token:"+realToken,
//               "1",
//               expire,
//               TimeUnit.MILLISECONDS
//       );
//
//
//    }


}
