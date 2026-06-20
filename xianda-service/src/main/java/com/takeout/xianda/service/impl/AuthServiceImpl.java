package com.takeout.xianda.service.impl;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.takeout.xianda.constant.JwtConstant;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.dto.RegisterDTO;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.exception.LoginException;

import com.takeout.xianda.mapper.UserMapper;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.AuthService;
import com.takeout.xianda.utils.JwtUtil;
import com.takeout.xianda.vo.CaptchaVO;
import com.takeout.xianda.vo.LoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result register(RegisterDTO dto) {
        //1,校验手机号是否注册
        User exist = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getPhone, dto.getPhone()));
        if (exist!= null){
            throw new LoginException(4004,"该手机号已注册");
        }

        //2，BCrypt加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(dto.getPassword());
        //3，分装实体
        User user = new User();
        BeanUtils.copyProperties(dto,user);
        user.setPassword(encode);
        userMapper.insert(user);
        return Result.success("注册成功");
    }

    @Override
    public LoginVO login(LoginDTO dto) {

        //0,校验验证码

        if (dto.getCodeKey() == null || dto.getCodeKey().isEmpty()) {
            throw new LoginException(4004, "验证码key不能为空");
        }
        if (dto.getCaptchaCode() == null || dto.getCaptchaCode().isEmpty()) {
            throw new LoginException(4005, "验证码不能为空");
        }

// 拼接redis key

        String redisKey = "captcha:" + dto.getCodeKey();

        String redisCode =(String) redisTemplate.opsForValue().get(redisKey);

        if (redisCode == null) {
            throw new LoginException(4006,"验证码已过期");
        }
// 比对【前端传入的验证码文本】和redis里存的验证码，不是比对codeKey
        if (!redisCode.equalsIgnoreCase(dto.getCaptchaCode())) {
            redisTemplate.delete(redisKey);
            throw new LoginException(4007,"验证码错误");
        }
//校验通过再删除key
        redisTemplate.delete(redisKey);



        //1,根据手机号查询 用户
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getPhone, dto.getPhone()));
        if (user == null) {
            throw new LoginException(4001,"该手机号未注册");
        }

        //2，BCrypt对比密码
        if(!BCrypt.checkpw(dto.getPassword(),user.getPassword())){
            throw new LoginException(4002,"密码错误");
        }

        //3，校验账号状态
        if(user.getStatus() == 0){
            throw new LoginException(4003,"账号被冻结，请联系管理员");
        }

        //4，生成JWT token
        String token = JwtUtil.createJwt(JwtConstant.SECRET_KEY, 60 * 60 * 1000 * 2, null);
        //5,封装返回VO
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole());
        return vo;


    }

//    token失效
    @Override
    public void logout(String token) {
        String realToken = token.replace("Bearer ", "");
       long expire = JwtUtil.getExpireTime(realToken)-System.currentTimeMillis();
       redisTemplate.opsForValue().set(
               "token:"+realToken,
               "1",
               expire,
               TimeUnit.MILLISECONDS
       );


    }

    @Override
    public Result<CaptchaVO> getCaptcha() {
        String codeKey = UUID.randomUUID().toString();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(110, 40, 4, 20);
        String code = lineCaptcha.getCode();
        String base64 = lineCaptcha.getImageBase64Data();

        redisTemplate.opsForValue().set(
                "captcha:"+codeKey,
                code,
                180,
                TimeUnit.SECONDS
        );
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCodeKey(codeKey);
        captchaVO.setImgBase64(base64);
        return Result.success(captchaVO);
    }


}
