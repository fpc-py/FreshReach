package com.takeout.xianda.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.takeout.xianda.constant.JwtConstant;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.dto.WxLoginDTO;
import com.takeout.xianda.entity.UserInfo;
import com.takeout.xianda.entity.WxMiniAppProperties;
import com.takeout.xianda.exception.LoginException;
import com.takeout.xianda.mapper.UserInfoMapper;
import com.takeout.xianda.service.UserLoginService;
import com.takeout.xianda.utils.JwtUtil;
import com.takeout.xianda.vo.UserLoginVO;
import com.takeout.xianda.vo.WxLoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.takeout.xianda.Interceptor.AuthInterceptor.TOKEN_BLACKLIST;

@Slf4j
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private  WxMiniAppProperties wxMiniAppProperties;

    @Override
    public UserLoginVO passwordLogin(LoginDTO loginDTO) {

        UserLoginVO vo = new UserLoginVO();
        BeanUtils.copyProperties(loginDTO,vo);

//        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<UserInfo>();
//        wrapper.eq(UserInfo::getPassword,loginDTO.getPhone());
//        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        //TODO 为什么selectOne失败，mapper手写sql成功？

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

    @Override
    public void logout(String token) {
        if (!jwtUtil.verifyToken(token)){
            throw new RuntimeException("无效token");
        }
        long expireTime = jwtUtil.getExpireTime(token);
        if (expireTime>0){
            stringRedisTemplate.opsForValue().set(TOKEN_BLACKLIST+token,"1",expireTime, TimeUnit.SECONDS);
        }
    }

    @Transactional
    @Override
    public WxLoginVO wxLogin(WxLoginDTO wxLoginDTO) {
        String appId = wxMiniAppProperties.getAppId();
        String appSecret = wxMiniAppProperties.getAppSecret();
        String code = wxLoginDTO.getCode();
        //1.请求微信服务器
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, appSecret, code);
        String response = HttpUtil.get(url);
        log.info("原始响应",response);
        Map<String,Object> wxResult = JSON.parseObject(response, Map.class);
        if (wxResult.get("errcode")!=null){
            throw new RuntimeException("微信登录失败"+wxResult.get("errmsg").toString());
        }
        String openid = (String) wxResult.get("openid");
        String sessionKey = (String) wxResult.get("session_key");

        //2.查询用户
        UserInfo info = userInfoMapper.selectOne(Wrappers.lambdaQuery(UserInfo.class)
                .eq(UserInfo::getOpenId, openid));

        //3.新用户自动注册
        if (info==null){
           info = new UserInfo();
            info.setOpenId(openid);
            info.setCreateTime(LocalDateTime.now());
            userInfoMapper.insert(info);
        }

        //4.生成jwt token,载荷放userId


        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", info.getId());
        String token = JwtUtil.createJwt(JwtConstant.SECRET_KEY, 60 * 60 * 1000 * 2, claims);
        //5.返回
        WxLoginVO vo = new WxLoginVO();
        vo.setToken(token);
        vo.setUserId(info.getId());
        vo.setName(info.getName());
        vo.setAvatar(info.getAvatar());
        return vo;

    }
}
